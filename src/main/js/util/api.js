export default function (url, method, onSuccess, onError) {
    fetch(`http://localhost:8080${url}`, {
        method,
        headers: {
            'api-token': localStorage.getItem('token')
        }
    })
        .then((data) => {
            if(data.status===403){
                localStorage.removeItem('token');
                location.href = "/auth";
            }

            if(!data.ok){
                onError(data);
            } else {
                data.json().then((data)=>onSuccess(data)).catch((e)=>onError(e))
            }
        });
}