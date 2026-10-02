let recruiterId = null;
const STATUSES = ['APPLIED', 'SHORTLISTED', 'SELECTED', 'REJECTED'];

async function init() {
    const me = await requireRole('RECRUITER');
    if (!me) return;
    recruiterId = me.userId;
    loadDrives();
}

function showMsg(text, type) {
    const box = document.getElementById('formMsg');
    box.textContent = text;
    box.className = `alert alert-${type}`;
}

document.getElementById('driveForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const body = {
        companyName: document.getElementById('companyName').value,
        jobTitle: document.getElementById('jobTitle').value,
        packageLpa: document.getElementById('packageLpa').value,
        minCgpa: document.getElementById('minCgpa').value,
        allowedBacklogs: document.getElementById('allowedBacklogs').value
    };
    const res = await fetch('/api/drives', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(body)
    });
    if (res.ok) {
        showMsg('Drive posted. It becomes visible to students once an admin approves it.', 'success');
        e.target.reset();
        loadDrives();
    } else {
        showMsg(await readError(res, 'Could not post the drive.'), 'danger');
    }
});

async function loadDrives() {
    const res = await fetch('/api/drives/mine', { credentials: 'include' });
    const drives = await res.json();
    const container = document.getElementById('drivesContainer');
    container.innerHTML = '';

    if (drives.length === 0) {
        container.innerHTML = '<div class="ch-empty">No drives yet. Post your first drive using the form.</div>';
        return;
    }

    drives.forEach(drive => {
        const card = document.createElement('div');
        card.className = 'ch-card mb-3';
        card.innerHTML = `
            <div class="d-flex justify-content-between align-items-start">
                <div>
                    <h5 style="font-family:'Fraunces',serif; margin-bottom:0.2rem;">${esc(drive.companyName)}</h5>
                    <p style="color:#6B7088; margin-bottom:0.8rem;">${esc(drive.jobTitle)} · ${esc(drive.packageLpa)} LPA · Min CGPA ${esc(drive.minCgpa)}</p>
                </div>
                <span class="badge-status badge-${drive.status.toLowerCase()}">${esc(drive.status)}</span>
            </div>
            <button class="btn btn-outline-dark btn-sm" data-id="${drive.id}">View applicants</button>
            <div class="applicants mt-3 d-none"></div>`;
        container.appendChild(card);

        const btn = card.querySelector('button');
        const box = card.querySelector('.applicants');
        btn.addEventListener('click', () => toggleApplicants(drive.id, box));
    });
}

async function toggleApplicants(driveId, box) {
    if (!box.classList.contains('d-none')) {
        box.classList.add('d-none');
        return;
    }
    const res = await fetch(`/api/applications/drive/${driveId}`, { credentials: 'include' });
    const apps = await res.json();
    box.classList.remove('d-none');

    if (apps.length === 0) {
        box.innerHTML = '<div class="ch-empty">No applications yet.</div>';
        return;
    }

    box.innerHTML = '';
    apps.forEach(app => {
        const row = document.createElement('div');
        row.className = 'ch-applicant';
        const options = STATUSES
            .map(s => `<option value="${s}" ${s === app.status ? 'selected' : ''}>${s}</option>`)
            .join('');
        row.innerHTML = `
            <div>
                <strong>${esc(app.studentName)}</strong><br>
                <small style="color:#6B7088;">${esc(app.branch)} · CGPA ${esc(app.cgpa)}</small>
            </div>
            <select class="form-select form-select-sm" style="width:auto;">${options}</select>`;
        box.appendChild(row);

        row.querySelector('select').addEventListener('change', async (e) => {
            await fetch(`/api/applications/${app.id}/status?status=${e.target.value}`, {
                method: 'PUT',
                credentials: 'include'
            });
        });
    });
}

init();