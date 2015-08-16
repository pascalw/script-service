var articles = [];

for(var i = 1 ; i <= 10; i++) {
  articles.push({
      title: 'Article ' + i,
      body: new Array(Math.floor(Math.random() * 5) + 1).join('lorem ipsum ').slice(0, -1)
  });
}

return articles;