function loadCat(id) {
	$('results').update('Carregando categoria...');
	new Ajax.Request(
			'category',
			{
				method : 'get',
				parameters : {
					id : id
				},
				onSuccess : function(transport) {
					$('results').update();
					createList(transport.responseJSON);
				},

				onFailure : function() {
					alert('Ocorreu um erro no servidor, por favor tente novamente mais tarde');
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

		$('results').insert(itemDiv);
	});
}