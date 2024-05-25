function CreateRequest()
{
    var Request = false;

    if (window.XMLHttpRequest)
    {
        //Gecko-совместимые браузеры, Safari, Konqueror
        Request = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        //Internet explorer
        try
        {
             Request = new ActiveXObject("Microsoft.XMLHTTP");
        }
        catch (CatchException)
        {
             Request = new ActiveXObject("Msxml2.XMLHTTP");
        }
    }

    if (!Request)
    {
        alert("Невозможно создать XMLHttpRequest");
    }
    else
    {
        console.log("request has been created");
    }

    return Request;
}

function SendRequest(r_method, r_path, r_args, r_handler, r_body)
{
    console.log("SendRequest " + r_method + "  " + r_path + "  " + r_args + "  " + r_body);
    //Создаём запрос
    var Request = CreateRequest();

    //Проверяем существование запроса еще раз
    if (!Request)
    {
        return;
    }

    //Назначаем пользовательский обработчик
    Request.onreadystatechange = function()
    {
        //Если обмен данными завершен
        if (Request.readyState == 4)
        {
            if (Request.status == 200)
            {
                //Передаем управление обработчику пользователя
                r_handler(Request);
            }
        }
    }

    //Проверяем, если требуется сделать GET-запрос
    //if (r_method.toLowerCase() == "get" && r_args.length > 0)
    r_path += "?" + r_args;

    //Инициализируем соединение
    Request.open(r_method, r_path, true);

    if (r_method.toLowerCase() == "post")
    {
        //Если это POST-запрос

        //Устанавливаем заголовок
        Request.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        //Посылаем запрос
        Request.send(r_body);
    }
    else
    {
        //Если это GET-запрос

        //Посылаем нуль-запрос
        Request.send(null);
    }
}


function getCurrentUrl() {
    return window.location.href;
}

console.log("javascript");

article = document.querySelector('#article');

//var articleName = [[${name}]];
console.log(articleName);

const commentIndent = 3;

let comments_title_span = document.createElement('span');
comments_title_span.style.cssText += 'width: 100%;';
let comments_title_h2 = document.createElement('h2')
comments_title_h2.classList.add('article_h2');
comments_title_h2.innerHTML = 'Комментарии';
comments_title_h2.style.cssText += 'display: inline-block; width: fit-content;';
comments_title_span.appendChild(comments_title_h2);

let btnRefresh = document.createElement('button');
btnRefresh.classList.add('comment_btn_answer');
btnRefresh.classList.add('btn');
btnRefresh.classList.add('btn-secondary');
btnRefresh.cssText += 'display: inline-block;'
btnRefresh.innerHTML = 'Обновить';
comments_title_span.appendChild(btnRefresh);

var Handler = function(Request)
{
    var responseData = eval("(" + Request.responseText + ")")
    console.log(responseData);
    if(responseData === undefined) {
        console.log("responseData is undefined");
        return;
    }

    for(let i = 0; i < responseData.length; i++) {
        let c = responseData[i];
        console.log(c);
        addCommentToComments(c);
        if(new Date(c.date) > new Date(date))
            date = c.date;

    }
    comments_div.innerHTML = '';
    createComments();

    console.log("date = " + date);
}


btnRefresh.addEventListener('click', function() {
    SendRequest("GET", getCurrentUrl() + '/new_comments'
                          , '&date=' + date
                          , Handler
                          , '');
});
article.appendChild(comments_title_span);

let comments_div = document.createElement('div');
comments_div.id = 'comments_div';
article.appendChild(comments_div);


function btnSendAnswerClick(level, comment_text, par_id) {
    console.log("level = " + level);
    console.log("comment_text = " + comment_text);
    console.log("par_id = " + par_id);



    //Отправляем запрос
    SendRequest("POST", getCurrentUrl() + '/new_comment'
                      , 'par_id=' + par_id + '&date=' + date
                      , Handler
                      , comment_text);

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

function addCommentToComments(data) {
    for(let i = 0; i < comments.length; i++) {
        if(comments[i].comment.id == data.comment.id)
            return;
    }

    if(data.comment.parent_id == -1) {
        comments.push(data);
    }
    else {
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
    }
}

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