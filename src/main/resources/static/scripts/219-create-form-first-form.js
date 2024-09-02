document.querySelectorAll('#firstForm, #delete-form').forEach(form => {
    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const form = event.target;
        const formData = new FormData(form);

        const _method = formData.get('_method');

        if (_method != null)
            formData.set('_method', _method);

        const basicAuthorizationToken = localStorage.getItem('basicAuthorizationToken');

        const headers = basicAuthorizationToken == null ? null : {
            'Authorization': 'Basic ' + basicAuthorizationToken
        };

        (headers != null ?
            fetch(form.action, {
                method: form.method,
                body: formData,
                headers: headers
            }) : fetch(form.action, {
                method: form.method,
                body: formData
            }))
            .then(response => {
                return response.text();
            })
            .then(htmlContent => {
                document.open();
                document.write(htmlContent);
                document.close();
            })
    });
});