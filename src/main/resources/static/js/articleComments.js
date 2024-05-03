console.log("javascript");

article = document.querySelector('#article');

//var articleName = [[${name}]];
console.log(articleName);

const commentIndent = 30;

let new_comment_div = document.createElement('div');
new_comment_div.classList.add('article');
new_comment_div.classList.add('comment');
new_comment_div.style.cssText += 'margin-left: ' + commentIndent.toString() + 'px;';
new_comment_div.style.cssText += 'margin-left: 0px;';

add_btn_answer(new_comment_div, -1);
article.appendChild(new_comment_div);

function add_btn_answer(div, par_id) {
    let btnAnswer = document.createElement('button');
    btnAnswer.classList.add('comment_btn_answer');
    btnAnswer.classList.add('btn');
    btnAnswer.classList.add('btn-secondary');
    if(par_id == null || par_id < 0)
        btnAnswer.innerHTML = 'Оставить комментарий';
    else
        btnAnswer.innerHTML = 'Ответить';
    div.appendChild(btnAnswer);

    btnAnswer.addEventListener('click', function() {
        btnAnswer.remove();

        let formAnswer = document.createElement('form');
        formAnswer.action = '/article/' + articleName + '/new_comment';
        formAnswer.method = 'post';
        div.appendChild(formAnswer);

        let p1 = document.createElement('p');
        let inputTextarea = document.createElement('textarea');
        inputTextarea.classList.add('comment_input_text');
        inputTextarea.name = "comment_text";
        inputTextarea.rows = 5;
        inputTextarea.cols = 33;
        p1.appendChild(inputTextarea);
        formAnswer.appendChild(p1);

        let inputParId = document.createElement('input');
        inputParId.type = 'hidden';
        inputParId.name = 'par_id';
        inputParId.value = par_id;
        formAnswer.appendChild(inputParId);

        let btnSendAnswer = document.createElement('button');
        btnSendAnswer.classList.add('comment_btn_answer');
        btnSendAnswer.innerHTML = 'Отправить';
        btnSendAnswer.type = 'submit';
        btnSendAnswer.classList.add('btn');
        btnSendAnswer.classList.add('btn-success');
        formAnswer.appendChild(btnSendAnswer);

        /*btnSendAnswer.addEventListener('click', function() {
            //let text =
             btnSendAnswer.remove();
             inputTextarea.remove();
             add_btn_answer(div);
        });*/
    });
}

//let input = document.createElement('input');
//input.dataset.right = el.right_answer;
//div.appendChild(input);
//input.classList.add('textInput');
//new_comment_div.appendChild(input);

for(let comment of comments) {
    let div = document.createElement('div');
    div.classList.add('article');
    div.classList.add('comment');
    div.style.cssText += 'margin-left: ' + (commentIndent * comment.level).toString() + 'px;';
    div.style.cssText += 'margin-top: 5px;';
    div.setAttribute('id', 'comment' + comment.comment.id);
    article.appendChild(div);

    let p1 = document.createElement('p');
    p1.classList.add('comment_p');

    if(comment.comment.parent_id != null && comment.comment.parent_id >= 0) {
        p1.innerHTML = 'user ' + comment.userName;
        p1.innerHTML += '  ответил на <a href=\"#comment' + comment.comment.parent_id + '\" class=\"simple_link\"> комментарий</a>';
    } else {
        p1.innerHTML = 'user ' + comment.userName + ':';
    }

    div.appendChild(p1);

    let p2 = document.createElement('p');
    p2.classList.add('comment_p');
    let sdate =  comment.comment.date.slice(0, 10);
    p2.innerHTML = "   Дата отправки: " + sdate;
    div.appendChild(p2);

    let p3 = document.createElement('p');
    p3.classList.add('comment_p');
    p3.innerHTML = comment.comment.text_html;
    div.appendChild(p3);

    add_btn_answer(div, comment.comment.id);
}