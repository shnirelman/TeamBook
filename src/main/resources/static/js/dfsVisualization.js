let canvas = document.getElementById("drawing");
let ctx = canvas.getContext('2d');

let n = 0;
let dfsVertex = -1;
let grey = [];
let isDirected = 1;

let X = new Array();
let Y = new Array();
let vertices = new Array();
const R = 20;

let g = new Array();

function drawVertex(v) {
    ctx.beginPath();
    ctx.arc(X[v], Y[v], R, 0, 2 * Math.PI);

    if(grey[v] == 1)
        ctx.fillStyle = 'grey';
    else ctx.fillStyle = 'white';

    ctx.fill();
    ctx.lineWidth = 3;

    if(v != dfsVertex)
        ctx.strokeStyle = 'black';
    else ctx.strokeStyle = 'blue';

    ctx.stroke();


    ctx.fillStyle = 'black';
    ctx.font = '25px sans';
    ctx.fillText(v + 1, X[v] - 2 - 5 * (v + 1).toString().length, Y[v] + 7);
}

function addVertex(x, y) {
    X[n] = x;
    Y[n] = y;
    vertices[n] = n;
    g[n] = new Array();
    n++;
}

function isInt(x) {
    return /^\d+$/.test(x);
}

function isVertex(x, n) {
    return (isInt(x) && x >= 1 && x <= n);
}

function deleteVertex(v) {
    if(isVertex(v, n)) {
        if(v >= 1 && v <= n) {
            v--;
            for(let i = v + 1; i < n; i++) {
                X[i - 1] = X[i];
                Y[i - 1] = Y[i];
            }

            let g2 = new Array();

            for(let i = 0; i < n - 1; i++)
                g2[i] = new Array();

            for(let i = 0; i < n; i++)
                for(let j = 0; j < n; j++)
                    if(g[i][j] >= 1 && i != v && j != v) {
                        let ni = i;
                        let nj = j;

                        if(ni > v)
                            ni--;

                        if(nj > v)
                            nj--;

                        g2[ni][nj] = 1;
                    }

            g = g2;


            let i = 0;

            while(vertices[i] != v)
                i++;

            for(let j = i + 1; j < n; j++)
                vertices[j - 1] = vertices[j];

            for(let i = 0; i < n; i++)
                if(vertices[i] > v)
                    vertices[i]--;

            n--;
        }
    }
}

function drawEdge(v, u) {
    ctx.lineWidth = 3;

    if(g[v][u] == 1)
        ctx.strokeStyle = 'black';
    else ctx.strokeStyle = 'red';

    if(isDirected == 0) {
        if(g[u][v] == 2 || g[u][v] == 1 && g[v][u] == 1 && u > v)
            return;

        ctx.beginPath();
        ctx.moveTo(X[v], Y[v]);
        ctx.lineTo(X[u], Y[u]);
        ctx.stroke();
    }
    else if((X[v] - X[u]) * (X[v] - X[u]) + (Y[v] - Y[u]) * (Y[v] - Y[u]) > 4 * R * R) {
        let dx = X[v] - X[u];
        let dy = Y[v] - Y[u];
        let len = Math.sqrt(dx * dx + dy * dy);
        let x = X[u] + dx / len * R;
        let y = Y[u] + dy / len * R;

        let shx = 0, shy = 0;

        if(g[u][v] >= 1) {
            shx = -dy / len * 8;
            shy = dx / len * 8;
        }

        ctx.beginPath();
        ctx.moveTo(X[v] + shx, Y[v] + shy);
        ctx.lineTo(X[u] + shx, Y[u] + shy);
        ctx.stroke();

        x += shx;
        y += shy;

        let a = Math.PI / 6;
        ctx.beginPath();
        ctx.moveTo(x + (dx * Math.cos(a) - dy * Math.sin(a)) / len * 20,
                   y + (dx * Math.sin(a) + dy * Math.cos(a)) / len * 20);
        ctx.lineTo(x, y);
        ctx.lineTo(x + (dx * Math.cos(-a) - dy * Math.sin(-a)) / len * 20,
                   y + (dx * Math.sin(-a) + dy * Math.cos(-a)) / len * 20);
        ctx.stroke();
    }
}

function addEdge(v, u) {
    if(v != u && isVertex(v, n) && isVertex(u, n)) {
        u--;
        v--;
        g[v][u] = 1;
    }
}

function deleteEdge(v, u) {
    if(isVertex(v, n) && isVertex(u, n)) {
        u--;
        v--;

        g[v][u] = 0;

        if(isDirected == 0)
            g[u][v] = 0;
    }
}

function draw() {
    ctx.fillStyle = 'aquamarine';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    for(let i = 0; i < n; i++)
        for(let j = 0; j < n; j++)
            if(g[i][j] >= 1)
                drawEdge(i, j);

    for(let i = 0; i < n; i++)
        drawVertex(vertices[i]);
}

function deleteGraph() {
    g = [];
    vertices = [];
    n = 0;
    draw();
}

function loadExample() {
    for(let i = 150; i <= 350; i += 50)
        addVertex(i, i);

    for(let i = 1; i < n; i++)
        addEdge(i, i + 1);

    draw();
}

loadExample();

let movingVertex = -1;

canvas.addEventListener("mousedown", (e) => {
    let x = e.offsetX;
    let y = e.offsetY;

    let v = -1;

    for(let i = 0; i < n; i++) {
        let u = vertices[i];
        if((X[u] - x) * (X[u] - x) + (Y[u] - y) * (Y[u] - y) <= R * R)
            v = u;
    }

    if(v != -1) {
        let i = 0;

        while(vertices[i] != v)
            i++;

        for(let j = i + 1; j < n; j++)
            vertices[j - 1] = vertices[j];

        vertices[n - 1] = v;
        movingVertex = v;
        draw();
    }
});

document.addEventListener("mousemove", (e) => {
    if(movingVertex != -1) {
        let rect = canvas.getBoundingClientRect();
        X[movingVertex] = e.clientX - rect.left;
        Y[movingVertex] = e.clientY - rect.top;
        draw();
    }
});

document.addEventListener("mouseup", (e) => {
    movingVertex = -1;
});

let btnDeleteGraph = document.getElementById("btnDeleteGraph");
btnDeleteGraph.addEventListener("click", function() {
    clearCurDfs();
    deleteGraph();
});

let btnLoadExample = document.getElementById("btnLoadExample");
btnLoadExample.addEventListener("click", function() {
    clearCurDfs();
    deleteGraph();
    loadExample();
});

let radioDirected = document.getElementById("radioDirected");
let radioUndirected = document.getElementById("radioUndirected");

radioDirected.addEventListener("click", function() {
    if(isDirected == 0) {
        clearCurDfs();
        isDirected = 1;
        draw();
    }
});

radioUndirected.addEventListener("click", function() {
    if(isDirected == 1) {
        clearCurDfs();
        isDirected = 0;
        draw();
    }
});

let btnAddVertex = document.getElementById("btnAddVertex");
let newVertex = 0;

btnAddVertex.addEventListener("click", function() {
    clearCurDfs();
    let cx = canvas.width / 2;
    let cy = canvas.height / 2;
    let R = Math.min(cx, cy) - 50;

    addVertex(cx + R * Math.cos(newVertex), cy + R * Math.sin(newVertex));
    newVertex++;

    draw();
});

let btnDeleteVertex = document.getElementById("btnDeleteVertex");
let txtDeleteVertex = document.getElementById("txtDeleteVertex");

btnDeleteVertex.addEventListener("click", function() {
    clearCurDfs();
    deleteVertex(txtDeleteVertex.value);

    draw();
});

let btnAddEdge = document.getElementById("btnAddEdge");
let txtAddEdge1 = document.getElementById("txtAddEdge1");
let txtAddEdge2 = document.getElementById("txtAddEdge2");

btnAddEdge.addEventListener("click", function() {
    clearCurDfs();
    addEdge(txtAddEdge1.value, txtAddEdge2.value);

    draw();
});

let btnDeleteEdge = document.getElementById("btnDeleteEdge");
let txtDeleteEdge1 = document.getElementById("txtDeleteEdge1");
let txtDeleteEdge2 = document.getElementById("txtDeleteEdge2");

btnDeleteEdge.addEventListener("click", function() {
    clearCurDfs();
    deleteEdge(txtDeleteEdge1.value, txtDeleteEdge2.value);

    draw();
});

let used = new Array();
let steps = new Array();
let step = 0;
let indDfs = 0;

function clearCurDfs() {
    grey = [];
    dfsVertex = -1;
    indDfs++;
    steps = [];
    step = 0;
    used = [];

    for(let i = 0; i < n; i++)
        for(let j = 0; j < n; j++)
            if(g[i][j] == 2)
                g[i][j] = 1;
}

function dfs(v) {
    used[v] = 1;
    steps[step] = ['used', v];
    step++;

    for(let i = 0; i < n; i++) {
        if(g[v][i] >= 1 || g[i][v] >= 1 && isDirected == 0) {
            if(g[v][i] >= 1) {
                if(used[i] != 1)
                    steps[step] = [v, i, 1];
                else steps[step] = [v, i];
            }
            else {
                if(used[i] != 1)
                    steps[step] = [i, v, 1];
                else steps[step] = [i, v];
            }


            step++;

            if(used[i] != 1)
            {
                dfs(i);
                steps[step] = [v];
                step++;
            }
        }
    }
}

function dfsIter(curDfs, i) {
    if(indDfs > curDfs)
        return;

    if(i == step) {
        dfsVertex = -1;

        for(let i = 0; i < n; i++)
            grey[i] = 0;

        draw();
        return;
    }

    let x = steps[i];

    if(x.length == 1) {
        dfsVertex = x[0];
        draw();
        setTimeout(dfsIter, 600, curDfs, i + 1);
        return;
    }

    if(x[0] == 'used') {
        grey[x[1]] = 1;
        draw();
        setTimeout(dfsIter, 600, curDfs, i + 1);
        return;
    }

    g[x[0]][x[1]] = 2;

    draw();

    setTimeout(dfsClearIter, 600, curDfs, i);
}

function dfsClearIter(curDfs, i) {
    if(indDfs > curDfs)
        return;

    let x = steps[i];

    g[x[0]][x[1]] = 1;

    if(x[2] == 1)
        dfsVertex ^= x[0] ^ x[1];
    draw();

    setTimeout(dfsIter, 600, curDfs, i + 1);
}

function drawDfs(v) {
    clearCurDfs();
    dfs(v);

    dfsVertex = v;
    draw();

    setTimeout(dfsIter, 600, indDfs, 0);
}

let btnDfs = document.getElementById("btnDfs");
let txtDfs = document.getElementById("txtDfs");

btnDfs.addEventListener("click", function() {
    let v = txtDfs.value;

    if(isVertex(v, n))
        drawDfs(v - 1);
});

function handleFilesOpen(files) {
    clearCurDfs();
    draw();
    
    let file = files[0];

    let reader = new FileReader();

    reader.readAsText(file);

    reader.onload = function() {
        let text = reader.result;

        let nums = text.split(/\ |\n/).map(Number);

        let isDir = nums[0];
        let N = nums[1];
        let M = nums[2];

        for(let i = 0; i < 3; i++)
            if(!isInt(nums[i])) {
                return;
            }

        for(let i = 3; i < 3 + 2 * N; i++)
            if(isNaN(nums[i])) {
                return;
            }

        for(let i = 3 + 2 * N; i < 3 + 2 * N + 2 * M; i++) {
            if(!isVertex(nums[i], N)) {
                return;
            }
            nums[i]--;
        }

        isDirected = (isDir != 0);
        n = N;

        vertices = new Array();

        for(let i = 0; i < n; i++)
            vertices[i] = i;

        if(isDirected)
            radioDirected.checked = true;
        else radioUndirected.checked = true;

        g = new Array();
        grey = new Array();

        for(let i = 0; i < n; i++)
            g[i] = new Array();

        for(let i = 0; i < M; i++) {
            g[nums[3 + 2 * n + 2 * i]][nums[4 + 2 * n + 2 * i]] = 1;
        }

        X = new Array();
        Y = new Array();

        for(let i = 0; i < n; i++) {
            X[i] = nums[3 + 2 * i];
            Y[i] = nums[4 + 2 * i];
        }

        draw();
    };
}

let btnSave = document.getElementById('btnSave');

btnSave.addEventListener("click", function() {
    clearCurDfs();
    draw();

    let ans = "";

    ans += (isDirected ? 1 : 0);

    let m = 0;

    for(let i = 0; i < n; i++)
        for(let j = 0; j < n; j++)
            if(g[i][j] == 1)
                m++;

    ans += "\n" + n + ' ' + m + "\n";

    for(let i = 0; i < n; i++)
        ans += X[i] + ' ' + Y[i] + "\n";

    for(let i = 0; i < n; i++) {
        for(let j = 0; j < n; j++) {
            if(g[i][j] == 1)
                ans += (i + 1) + ' ' + (j + 1) + "\n";
        }
    }

    let linkSave = document.getElementById('linkSave');
    linkSave.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(ans));

    linkSave.click();
});

