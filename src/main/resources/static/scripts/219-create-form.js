document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const form = document.getElementById('loginForm');

    const firstForm = document.getElementById('firstForm');
    const firstFormData = new FormData(firstForm);

    const basicAuthorizationToken = b64EncodeUnicode(document.getElementById('username').value
        + ':' + document.getElementById('password').value);

    const _method = new FormData(document.getElementById('loginForm')).get('_method');

    if (_method != null)
        firstFormData.set('_method', _method);

    const headers = {
        'Authorization': 'Basic ' + basicAuthorizationToken
    };

    fetch(form.action, {
        method: form.method,
        body: firstFormData,
        headers: headers
    })
        .then(response => {
            if (response.ok) {
                localStorage.setItem('basicAuthorizationToken', basicAuthorizationToken);
            }
            return response.text();
        })
        .then(htmlContent => {
            document.open();
            document.write(htmlContent);
            document.close();
        })
});

function closeDialog() {
    document.getElementById('dialog').style.display = 'none';
}

window.onclick = function(event) {
    const dialog = document.getElementById('dialog');
    if (event.target === dialog) {
        dialog.style.display = "none";
    }
}

function b64EncodeUnicode(str) {
    const utf8Bytes = new TextEncoder().encode(str);
    const binaryString = Array.from(utf8Bytes, byte => String.fromCharCode(byte)).join('');
    return btoa(binaryString);
}