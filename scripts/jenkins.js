function() {
  var url = 'https://jenkins-ci.org/changelog';

  var page = fetchDocument(url);
  var feed = newFeed()
    .setTitle("")
    .setDescription("Jenkins Changelog")
    .setLink(url);

 var df = new java.text.SimpleDateFormat("yyyy/MM/dd");

  page.select('.content > h3').stream().forEach(function(version) {
    var title = version.text();
    var description = version.nextElementSibling().outerHtml();
    var publishedDateString = version.textNodes()[0].toString().replaceFirst("\\((.*)\\)", "$1"); // (yyyy/mm/dd) -> yyyy/mm/dd
    var publishedDate = df.parse(publishedDateString);
    var link = 'https://jenkins-ci.org/changelog#' + version.select('a').attr('name').toString();

    var entry = feed.newEntry()
      .setTitle(title)
      .setLink(link)
      .setPublishedDate(publishedDate)
      .setDescription(description);

    feed.addEntry(entry);
  });

  return feed;
}
