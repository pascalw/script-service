var url = 'https://www.hetzner.de/nl/hosting/news/news-home';
var page = fetchDocument(url);

var feed = newFeed()
  .setTitle("Hetzner news")
  .setDescription("Hetzner news")
  .setLink(url);

var nextOfType = function(element, type) {
  var node = element.nextElementSibling();
  while (node !== null && !node.tagName().equalsIgnoreCase(type)) {
      node = node.nextElementSibling();
  }
  return node;
};

var df = new java.text.SimpleDateFormat('dd MMMM yyyy', new java.util.Locale('en', ''));

page.select("#content-noright h4:gt(2)").stream().forEach(function(h4) {
  var title = h4.ownText();
  var nextP = nextOfType(h4, 'p');
  var description = nextP.text();
  var publishedDateString = h4.select('em').text().replace('from: ', '');
  var publishedDate = df.parse(publishedDateString);
  
  var link = nextP.siblingElements().select('a').first().attr('href').toString();

  var entry = feed.newEntry()
    .setTitle(title)
    .setLink(link)
    .setPublishedDate(publishedDate)
    .setDescription(description);

  feed.addEntry(entry);
});

return feed;
