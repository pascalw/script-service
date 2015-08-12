var url = 'http://rocketconf.eu';
var page = fetchDocument(url);

var feed = newFeed()
  .setTitle("Rocketconf")
  .setDescription("Rocketconf")
  .setLink(url);

page.select("#speakers ul li").stream().forEach(function(li) {
  var name = li.select("h3").text();
  var bio = li.select("h4").text();

  var entry = feed.newEntry()
    .setTitle(name)
    .setLink('rocketconf.eu')
    .setPublishedDate(new java.util.Date())
    .setDescription(bio);

  feed.addEntry(entry);
});

return feed;
