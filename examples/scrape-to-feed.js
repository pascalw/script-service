var scraper = require('scraper');
var feed    = require('feed');
var date    = require('date').parser('yyyy/MM/dd', 'en-US');

var url = 'https://jenkins-ci.org/changelog';
var page = scraper.scrape(url);

var feed = feed.newFeed()
  .setTitle('Jenkins Changelog')
  .setDescription('Jenkins Changelog')
  .setLink(url);

page.select('.content > h3').stream().forEach(function(version) {
  var title = version.select('a').text();
  var description = version.nextElementSibling().outerHtml();
  var publishedDateString = version.ownText().replaceAll('[\(\)]', '');
  var publishedDate = date.parse(publishedDateString);
  var link = url + '#' + version.select('a').attr('name').toString();

  var entry = feed.newEntry()
    .setTitle(title)
    .setLink(link)
    .setPublishedDate(publishedDate)
    .setDescription(description);

  feed.addEntry(entry);
});

return feed.build();
