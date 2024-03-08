test = document.querySelector('#test');

let maxQId = 0;

for(let el of questions) {
    if(el.id > maxQId)
        maxQId = el.id;
}

let answersForQ = new Array();

for(let i = 1; i <= maxQId; i++)
    answersForQ[i] = new Array();

for(let ans of answers) {
    answersForQ[ans.question_id].push(ans);
}

let id = 1;

for(let el of questions) {
    if(el.article_name == name) {
        let div = document.createElement('div');
        div.classList.add('question');
        test.appendChild(div);

        let p = document.createElement('p');
        p.innerHTML = id + '. ' + el.text;
        id++;
        div.appendChild(p);

        if(el.type == 1) {
            let input = document.createElement('input');
            input.dataset.right = el.right_answer;
            div.appendChild(input);
            input.classList.add('textInput');
        }
        else if(el.type == 2) {
            let answerIndex = 0;
            for(let answer of answersForQ[el.id]) {
                let divAnswer = document.createElement('div');
                divAnswer.classList.add('answer');
                div.appendChild(divAnswer);

                let radio = document.createElement('input');
                radio.type = 'radio';
                radio.name = el.id;
                radio.classList.add('radio');
                divAnswer.appendChild(radio);

                let label = document.createElement('label');
                label.classList.add('answerText');
                label.innerHTML = answer.text;
                divAnswer.appendChild(label);

                if(answerIndex == el.right_answer)
                    radio.dataset.isCorrect = '1';
                else radio.dataset.isCorrect = '0';

                answerIndex++;
            }
        }
        else if(el.type == 3) {
            let answerIndex = 0;
            for(let answer of answersForQ[el.id]) {
                let divAnswer = document.createElement('div');
                divAnswer.classList.add('answer');
                div.appendChild(divAnswer);

                let checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.classList.add('checkbox');
                divAnswer.appendChild(checkbox);

                let label = document.createElement('label');
                label.classList.add('answerText');
                label.innerHTML = answer.text;
                divAnswer.appendChild(label);

                if(el.right_answer.includes(answerIndex))
                    checkbox.dataset.isCorrect = '1';
                else checkbox.dataset.isCorrect = '0';

                answerIndex++;
            }
        }
    }
}

button = document.createElement('button');
button.id = 'button';
button.innerHTML = 'Проверить';
test.appendChild(button);