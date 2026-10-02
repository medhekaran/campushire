const errorBox = document.getElementById('errorBox');

function showError(message) {
    errorBox.textContent = message;
    errorBox.classList.remove('d-none');
}

const PASSWORD_RULE = /^(?=.*[A-Za-z])(?=.*\d).{8,72}$/;
const EMAIL_RULE = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;

// Returns an error message, or null when everything is fine
function validateRegister(body, confirmPassword) {
    if (body.name.trim().length < 2) return 'Name must be at least 2 characters';
    if (!EMAIL_RULE.test(body.email.trim())) return 'Enter a valid email address';
    if (!PASSWORD_RULE.test(body.password)) {
        return 'Password must be 8 to 72 characters with at least one letter and one number';
    }
    if (body.password !== confirmPassword) return 'Passwords do not match';
    return null;
}

const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorBox.classList.add('d-none');

        const body = {
            name: document.getElementById('name').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            role: document.getElementById('role').value
        };
        const confirmPassword = document.getElementById('confirmPassword').value;

        const problem = validateRegister(body, confirmPassword);
        if (problem) {
            showError(problem);
            return;
        }

        // Disable the button so a double click cannot send two requests
        const btn = document.getElementById('submitBtn');
        btn.disabled = true;

        const res = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            window.location.href = '/login';
        } else {
            const err = await res.json().catch(() => null);
            showError(err && err.message ? err.message : 'Registration failed');
            btn.disabled = false;
        }
    });
}

const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorBox.classList.add('d-none');

        const body = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(body)
        });
        if (res.ok) {
            const user = await res.json();
            if (user.role === 'STUDENT') window.location.href = '/student/dashboard';
            else if (user.role === 'RECRUITER') window.location.href = '/recruiter/dashboard';
            else window.location.href = '/admin/dashboard';
        } else {
            showError('Invalid email or password');
        }
    });
}