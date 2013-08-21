function load() {
	loadCategories();
}

function doSearch() {
	var post = $('find').serialize(true);
	$('find').disable();
	$('results').update('Carregando resultados...');
	new Ajax.Request(
			'../find',
			{
				method : 'get',
				parameters : post,
				onSuccess : function(transport) {
					$('results').update();
					createList(transport.responseJSON);
					$('find').enable();
				},

				on400 : function() {
					alert('Por favor verifique os dados com erro');
					$('find').enable();
				},

				on500 : function() {
					alert('Ocorreu um erro no servidor, por favor tente novamente mais tarde');
					$('find').enable();
				}

			});
	return false;
}

function createList(jsonArray) {
	jsonArray.each(function(item) {
		var itemDiv = createDiv('', 'item');
		itemDiv.setAttribute('id', item.id)

		itemDiv.insert(createArticleLink(item.eRepId, item.title));
		if (item.author != null) {
			itemDiv.insert(createAuthorLink(item.author));
		}
		itemDiv.insert(createDiv(item.eRepDay, 'date'));

		var catName = cats.get(item.category);
		itemDiv.insert(createDiv(catName, 'category'));

		$('results').insert(itemDiv);
	});
}