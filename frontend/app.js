const API_URL = "http://localhost:8084/api";

async function fetchOrders() {
    const res = await fetch(`${API_URL}/orders/all`);
    const orders = await res.json();
    orders.sort((a, b) => a.id - b.id);
    const tbody = document.querySelector('#orders-table tbody');
    tbody.innerHTML = '';
    orders.forEach(order => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${order.id}</td>
            <td>${order.senderId}</td>
            <td>${order.receiverId}</td>
            <td>${order.transactionAmount}</td>
            <td>${order.orderStatus}</td>
        `;
        tbody.appendChild(tr);
    });
}

document.getElementById('order-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const senderId = +document.getElementById('senderId').value;
    const receiverId = +document.getElementById('receiverId').value;
    const transactionAmount = +document.getElementById('transactionAmount').value;
    await fetch(`${API_URL}/orders/add`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ senderId, receiverId, transactionAmount })
    });
    e.target.reset();
    fetchOrders();
});

async function fetchAccounts() {
    const res = await fetch(`${API_URL}/payments/all`);
    const accounts = await res.json();
    accounts.sort((a, b) => a.userId - b.userId);
    const tbody = document.querySelector('#accounts-table tbody');
    tbody.innerHTML = '';
    accounts.forEach(acc => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${acc.userId}</td><td>${acc.account}</td>`;
        tbody.appendChild(tr);
    });
}

document.getElementById('account-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = +document.getElementById('newAccountId').value;
    await fetch(`${API_URL}/payments/new/${id}`, { method: 'PUT' });
    e.target.reset();
    fetchAccounts();
});

document.getElementById('put-money-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = +document.getElementById('putMoneyId').value;
    const amount = +document.getElementById('putMoneyAmount').value;
    await fetch(`${API_URL}/payments/put/money/${id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(amount)
    });
    e.target.reset();
    fetchAccounts();
});

fetchOrders();
fetchAccounts();

setInterval(fetchOrders, 5000);
setInterval(fetchAccounts, 5000); 