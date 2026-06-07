function logoutAndRedirect() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");

    alert("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");

    window.location.href = "index.html";
}

async function authFetch(url, options = {}) {

    const token = localStorage.getItem("token");

    options.headers = {
        ...(options.headers || {}),
        "Authorization": "Bearer " + token
    };

    const response = await fetch(url, options);

    if (response.status === 401) {
        logoutAndRedirect();
        throw new Error("Token expired");
    }

    return response;
}