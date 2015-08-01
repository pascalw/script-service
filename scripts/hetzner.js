function(page) {
  var feed = newFeed()
    .setTitle("Hetzner news")
    .setDescription("Hetzner news")
    .setLink("https://www.hetzner.de/nl/hosting/news/news-home");

  var nextOfType = function(element, type) {
    var node = element.nextElementSibling();
    while (node != null && !node.tagName().equalsIgnoreCase(type)) {
        node = node.nextElementSibling();
    }
    return node;
  };

  page.select("#content-noright h4:gt(2)").stream().forEach(function(h4) {
    var title = h4.text();
    var nextP = nextOfType(h4, 'p');
    var description = nextP.text();
    var link = nextP.siblingElements().select('a').first().attr('href').toString();

    var entry = feed.newEntry()
      .setTitle(title)
      .setLink(link)
      .setPublishedDate(new java.util.Date())
      .setDescription(description);

    feed.addEntry(entry);
  });

  return feed;
}
