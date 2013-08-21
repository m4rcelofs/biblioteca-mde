function add() {
	var post = $('addArticle').serialize(true);
	$('addArticle').disable();
	new Ajax.Request(
			'../add',
			{
				parameters : post,
				onSuccess : function(transport) {
					alert(transport.responseJSON.message);
					$('addArticle').reset();
					$('addArticle').enable();
				},

				on400 : function() {
					alert('Por favor verifique os dados com erro');
					$('addArticle').enable();
				},

				on500 : function() {
					alert('Ocorreu um erro no servidor, por favor tente novamente mais tarde');
					$('addArticle').enable();
				}

			});
	return false;
}