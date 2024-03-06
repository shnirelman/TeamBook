button = document.querySelector('#button');

button.addEventListener('click', function() {
    let textInputs = document.querySelectorAll('#test input.textInput');

    for(let input of textInputs) {
        input.classList.remove('correct');
        input.classList.remove('incorrect');

        if(input.value == input.dataset.right) {
            input.classList.add('correct');
        }
        else {
            input.classList.add('incorrect');
        }
    }

    let radioInputs = document.querySelectorAll('#test input.radio');

    for(let input of radioInputs) {
        input.parentElement.classList.remove('correct');
        input.parentElement.classList.remove('incorrect');

        if(input.checked) {
            if(input.dataset.isCorrect == '1')
                input.parentElement.classList.add('correct');
            else input.parentElement.classList.add('incorrect');
        }
    }

    let checkboxInputs = document.querySelectorAll('#test input.checkbox');

    for(let input of checkboxInputs) {
        input.parentElement.classList.remove('correct');
        input.parentElement.classList.remove('incorrect');

        if(input.checked) {
            if(input.dataset.isCorrect == '1')
                input.parentElement.classList.add('correct');
            else input.parentElement.classList.add('incorrect');
        }
        else {
            if(input.dataset.isCorrect == '1')
                input.parentElement.classList.add('incorrect');
        }
    }
});