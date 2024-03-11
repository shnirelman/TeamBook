console.log("javascript")

article = document.querySelector('#article');

const commentIndent = 30;

for(let comment of comments) {
    let div = document.createElement('div');
    div.classList.add('article');
    div.classList.add('comment');
    div.style.cssText += 'margin-left: ' + (commentIndent * comment.level).toString() + 'px;';
    div.setAttribute('id', 'comment' + comment.comment.id);
    article.appendChild(div);

    let p1 = document.createElement('p');
    p1.classList.add('comment_p');
    p1.innerHTML = 'user id : ' + comment.comment.user_id;
    if(comment.comment.parent_id != null)
        p1.innerHTML += '  ответил на <a href=\"#comment' + comment.comment.parent_id + '\"> комментарий</a>';
    div.appendChild(p1);

    let p2 = document.createElement('p');
    p2.classList.add('comment_p');
    p2.innerHTML = "   Дата отправки: " + comment.comment.date;
    div.appendChild(p2);

    let p3 = document.createElement('p');
    p3.classList.add('comment_p');
    p3.innerHTML = comment.comment.text;
    div.appendChild(p3);
}