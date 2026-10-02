async function init() {
    const me = await requireRole('ADMIN');
    if (!me) return;
    loadPending();
}

async function loadPending() {
    const res = await fetch('/api/drives?status=PENDING', { credentials: 'include' });
    const drives = await res.json();
    const container = document.getElementById('pendingContainer');
    container.innerHTML = '';

    if (drives.length === 0) {
        container.innerHTML = '<div class="ch-empty">Nothing to review. New drives from recruiters will show up here.</div>';
        return;
    }

    drives.forEach(drive => {
        const card = document.createElement('div');
        card.className = 'ch-card mb-3 d-flex justify-content-between align-items-center';
        card.innerHTML = `
            <div>
                <h5 style="font-family:'Fraunces',serif; margin-bottom:0.2rem;">${esc(drive.companyName)}</h5>
                <p style="color:#6B7088; margin:0;">${esc(drive.jobTitle)} · ${esc(drive.packageLpa)} LPA · Min CGPA ${esc(drive.minCgpa)} · Backlogs allowed: ${esc(drive.allowedBacklogs)}</p>
            </div>
            <button class="btn btn-ch-primary btn-sm">Approve</button>`;
        container.appendChild(card);

        card.querySelector('button').addEventListener('click', async () => {
            const r = await fetch(`/api/drives/${drive.id}/approve`, { method: 'PUT', credentials: 'include' });
            if (r.ok) loadPending();
        });
    });
}

init();