package erep.mde;

import java.util.Locale;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

import erep.mde.dao.ErepUserDAO;
import erep.mde.data.Article;
import erep.mde.data.ErepUser;

public class IndexController {

	public boolean index(Article article) {
		Document doc = buildDocument(article, article.getContent().getValue());
		Index index = getIndex();

		PutResponse response = index.put(doc);
		return response.iterator().next().getCode() == StatusCode.OK;

	}

	public Results<ScoredDocument> search(String queryString) {
		QueryOptions opts = QueryOptions
				.newBuilder()
				.setLimit(15)
				.setFieldsToReturn("doc_id", "title", "category", "authorId",
						"authorName", "eRepDay").build();

		Query query = Query.newBuilder().setOptions(opts).build(queryString);
		return getIndex().search(query);
	}

	public Document buildDocument(Article article, String content) {
		ErepUserDAO uDao = new ErepUserDAO();
		try {
			ErepUser author = uDao.getUser(article.getAuthorId());
			if (author.geteRepId() == null)
				throw new IllegalStateException("Author has no eRep id");

			return Document
					.newBuilder()
					.setId(article.geteRepId())
					.addField(
							Field.newBuilder().setName("db_id")
									.setAtom(article.getId().toString()))
					.addField(
							Field.newBuilder().setName("title")
									.setText(article.getTitle()))
					.addField(
							Field.newBuilder()
									.setName("category")
									.setAtom(article.getCategoryId().toString()))
					.addField(
							Field.newBuilder().setName("authorId")
									.setAtom(author.geteRepId()))
					.addField(
							Field.newBuilder().setName("authorName")
									.setAtom(author.geteRepName()))
					.addField(
							Field.newBuilder().setName("description")
									.setText(article.getDescription()))
					.addField(
							Field.newBuilder().setName("content")
									.setText(content))
					.addField(
							Field.newBuilder().setName("eRepDay")
									.setNumber(article.geteRepDay()))
					.setLocale(new Locale("pt", "BR")).build();
		} finally {
			uDao.close();
		}
	}

	private Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder()
				.setName("articles_2012_11_14").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}

}
