console.log("javascript");

article = document.querySelector('#article');

//var articleName = [[${name}]];
console.log(articleName);

const commentIndent = 3;

let comments_div = document.createElement('div');
comments_div.id = 'comments_div';
article.appendChild(comments_div);

function btnSendAnswerClick(level, comment_text, par_id) {
    console.log("level = " + level);
    console.log("comment_text = " + comment_text);
    console.log("par_id = " + par_id);

    fetch(window.location.href + '/new_comment?comment_text=' + comment_text + '&par_id=' + par_id + '&level=' + level, {
      method: "POST"
    })
      .then(response=>response.json())
      .then(data=>{
        //comments.push(data);
        //comments.sort(function(a,b){
        //  return new Date(a.comment.date) - new Date(b.comment.date);
        //});
        if(data.comment.parent_id == -1) {
            comments.push(data);
        }
        else
        for(let i = 0; i < comments.length; i++) {
            console.log("i = " + i);
            if(i != -1) {
                console.log("id = " + comments[i].comment.id);
                console.log("par_id = " + data.comment.parent_id);
            }
            if(comments[i].comment.id == data.comment.parent_id) {
                console.log("if");

                for(let j = i + 1; j <= comments.length; j++) {
                    if(j == comments.length || comments[i].level >= comments[j].level) {
                        console.log("if2");
                        for(let k = comments.length - 1; k >= j; k--)
                            comments[k + 1] = comments[k];
                        comments[j] = data;
                        break;
                    }
                }
                break;
            }
        }
        console.log(data);
        console.log(comments);
        comments_div.innerHTML = '';
        createComments();
      })


}

function add_btn_answer(level, div, par_id) {
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

        let btnSendAnswer = document.createElement('button');
        btnSendAnswer.classList.add('comment_btn_answer');
        btnSendAnswer.innerHTML = 'Отправить';
        btnSendAnswer.type = 'button';
        btnSendAnswer.classList.add('btn');
        btnSendAnswer.classList.add('btn-success');
        btnSendAnswer.onclick = function() {
            btnSendAnswerClick(level + 1, inputTextarea.value, par_id);
        };
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

function createComments() {
    let new_comment_div = document.createElement('div');
    new_comment_div.classList.add('article');
    new_comment_div.classList.add('comment');
    //new_comment_div.style.cssText += 'margin-left: ' + commentIndent.toString() + '%;';
    new_comment_div.style.cssText += 'margin-left: 0px;';

    add_btn_answer(-1, new_comment_div, -1);
    comments_div.appendChild(new_comment_div);

    for(let comment of comments) {
        let div = document.createElement('div');
        div.classList.add('article');
        div.classList.add('comment');
        div.style.cssText += 'margin-left: ' + (commentIndent * comment.level).toString() + '%;';
        div.style.cssText += 'margin-top: 5px;';
        div.setAttribute('id', 'comment' + comment.comment.id);
        comments_div.appendChild(div);

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

        add_btn_answer(comment.level, div, comment.comment.id);
    }
}

createComments();