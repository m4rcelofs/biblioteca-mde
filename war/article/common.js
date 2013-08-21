var cats;

function loadCategories() {
	cats = new Hash();
	cats.set('4', 'Humor');
	cats.set('1003', 'Política');
	cats.set('1004', 'Internacional');
	cats.set('2003', 'Interação');
	cats.set('4004', 'Militar');
	cats.set('4005', 'Diplomacia / Alianças');
	cats.set('6006', 'Tutorial');
	cats.set('8003', 'Economia');
	cats.set('15001', 'Governo');
	cats.set('16001', 'Roleplay');
}

function createArticleLink(id, title) {

	var div = new Element('div');
	div.addClassName('articleLink')

	var attribs = {
		href : 'http://www.erepublik.com/en/article/{id}/1/20'.replace('{id}',
				id),
		target : '_blank'
	};
	var link = new Element('a', attribs);
	if (title) {
		link.update(title);
	} else {
		link.update(id);
	}
	div.insert(link);
	return div;
}

function createAuthorLink(sender) {

	var div = new Element('div');
	div.addClassName('sender')

	var attribs = {
		href : 'http://www.erepublik.com/en/citizen/profile/{id}'.replace(
				'{id}', sender.eRepId),
		target : '_blank'
	};
	var link = new Element('a', attribs);
	link.update(sender.name);

	div.insert(link);
	return div;
}

function createDiv(content, css) {
	var div = new Element('div');
	div.update(content);
	div.addClassName(css);
	return div;
}

function createButton(text, callback) {

	var btn = new Element('input', {
		type : 'submit',
		value : text,
		onclick : callback
	});

	return btn;
}