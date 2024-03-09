console.log("javascript")

article = document.querySelector('#article');

for(let comment of comments) {
    let div = document.createElement('div');
    div.classList.add('article');
    div.classList.add('comment');
    article.appendChild(div);

    let p1 = document.createElement('p');
    p1.classList.add('comment_p');
    p1.innerHTML = 'user id : ' + comment.user_id;
    div.appendChild(p1);

    let p2 = document.createElement('p');
    p2.classList.add('comment_p');
    p2.innerHTML = comment.text;
    div.appendChild(p2);
}