function load() {
	loadCategories();
	loadStatistics();
	loadArticles();
}

function loadStatistics() {
	new Ajax.Request('statistics', {
		method : 'GET',
		onSuccess : function(transport) {
			$('statistics').update(createStats(transport.responseJSON));
		},

		onFailure : function() {
			$('statistics').update('Erro ao carregar a lista de artigos');
		}
	});
}

function createStats(json) {

	var stats = new Element('div');

	var waiting = new Element('div');
	waiting.update('Artigos na fila: ' + json.waiting);

	var lastMinute = new Element('div');
	lastMinute.update('Indexados no último minuto: ' + json.executedLastMinute);

	stats.insert(waiting);
	stats.insert(lastMinute);

	return stats;

}

function loadArticles() {
	new Ajax.Request('list', {
		method : 'GET',
		onSuccess : function(transport) {
			$('loading').hide();
			createList(transport.responseJSON);
		},

		onFailure : function() {
			$('loading').update('Erro ao carregar a lista de artigos');
		}
	});
}

function createList(jsonArray) {
	jsonArray.each(function(item) {
		var itemDiv = createDiv('', 'item');
		itemDiv.setAttribute('id', item.id)

		itemDiv.insert(createArticleLink(item.erepId));
		if (item.sender != null) {
			itemDiv.insert(createAuthorLink(item.sender));
		}
		itemDiv.insert(createDiv(item.sentOn, 'sentOn'));
		itemDiv.insert(createDiv(item.description, 'description'));
		var catName = cats.get(item.category);
		itemDiv.insert(createDiv(catName, 'category'));

		itemDiv.insert(createButton('Aprovar', 'approve(' + item.id + ');'));
		itemDiv.insert(createButton('Rejeitar', 'reject(' + item.id + ');'));

		$('list').insert(itemDiv);
	});
}

function approve(id) {
	update('approve', id);
}

function reject(id) {
	update('reject', id);
}

function update(url, id) {
	new Ajax.Request(url, {
		parameters : {
			articleId : id
		},
		onSuccess : function(transport) {
			$(id.toString()).hide();
		},

		onFailure : function() {
			alert('Não foi possível atualizar o artigo');
		}
	});
}