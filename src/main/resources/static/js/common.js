// Escape text before putting it inside innerHTML (prevents script injection)
function esc(text) {
    const div = document.createElement('div');
    div.textContent = text == null ? '' : text;
    return div.innerHTML;
}

async function logout() {
    await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
    window.location.href = '/login';
}

// Returns {userId, role} or redirects to /login
async function requireRole(expectedRole) {
    const res = await fetch('/api/auth/me', { credentials: 'include' });
    if (!res.ok) { window.location.href = '/login'; return null; }
    const me = await res.json();
    if (me.role !== expectedRole) { window.location.href = '/login'; return null; }
    return me;
}