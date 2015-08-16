var feed = require('feed').fetch('https://feeds.feedburner.com/pwiddershoven?format=xml');

return feed.filterEntries(function(e) {
   return e.title.contains('Airplay');
}).build('atom_1.0');
