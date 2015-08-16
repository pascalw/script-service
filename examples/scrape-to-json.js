var scraper = require('scraper');
var date    = require('date').parser('dd MMMM yyyy', 'en_US');
var toList  = java.util.stream.Collectors.toList;

var doc = scraper.scrape('http://pwiddershoven.nl/');

return doc.select('.blogpost').stream().map(function(p) {
    var title = p.select('h2').text();
    var body = p.select('> p').html();
    var url = p.select('h2 a').attr('abs:href');

    var dateString = p.select('.postdate').text().replaceFirst('Posted on ', '');
    var publishedAt = date.parse(dateString);

    return {title: title, body: body, url: url, publishedAt: publishedAt};
}).collect(toList());
