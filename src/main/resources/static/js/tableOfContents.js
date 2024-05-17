table = document.querySelector('#table_of_contents');

let ol = document.createElement('ol');
for(let chapter of chapters) {
    ol.innerHTML += '<li><a href=' + chapter.code + ' class="link">' + chapter.name + "</a></li>";
}

table.appendChild(ol);