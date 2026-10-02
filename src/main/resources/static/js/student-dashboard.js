let profile = null;

function showMsg(id, text, type) {
    const box = document.getElementById(id);
    box.textContent = text;
    box.className = `alert alert-${type}`;
}

async function init() {
    const me = await requireRole('STUDENT');
    if (!me) return;
    await loadProfile();
    await refresh();
}

async function loadProfile() {
    const res = await fetch('/api/students/me', { credentials: 'include' });
    if (res.ok) {
        profile = await res.json();
        document.getElementById('branch').value = profile.branch;
        document.getElementById('cgpa').value = profile.cgpa;
        document.getElementById('backlogs').value = profile.backlogs;
    } else {
        profile = null;
        showMsg('profileMsg', 'Save your profile to see the drives you are eligible for.', 'info');
    }
}

document.getElementById('profileForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const body = {
        branch: document.getElementById('branch').value,
        cgpa: document.getElementById('cgpa').value,
        backlogs: document.getElementById('backlogs').value
    };
    const res = await fetch('/api/students/me', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(body)
    });
    if (res.ok) {
        profile = await res.json();
        showMsg('profileMsg', 'Profile saved.', 'success');
        refresh();
    } else {
        showMsg('profileMsg', await readError(res, 'Could not save the profile.'), 'danger');
    }
});

async function refresh() {
    if (!profile) {
        renderDrives([], []);
        renderApplications([]);
        return;
    }
    const [drivesRes, appsRes] = await Promise.all([
        fetch('/api/drives/eligible', { credentials: 'include' }),
        fetch('/api/applications/mine', { credentials: 'include' })
    ]);
    const drives = await drivesRes.json();
    const apps = await appsRes.json();
    renderDrives(drives, apps);
    renderApplications(apps);
}

function renderDrives(drives, apps) {
    const appliedIds = new Set(apps.map(a => a.driveId));
    const container = document.getElementById('drivesContainer');
    container.innerHTML = '';

    if (drives.length === 0) {
        container.innerHTML = '<div class="col-12"><div class="ch-empty">No open drives match your profile right now.</div></div>';
        return;
    }

    drives.forEach(drive => {
        const applied = appliedIds.has(drive.id);
        const col = document.createElement('div');
        col.className = 'col-md-6';
        col.innerHTML = `
            <div class="ch-card h-100">
                <h5 style="font-family:'Fraunces',serif;">${esc(drive.companyName)}</h5>
                <p style="color:#6B7088; margin-bottom:1rem;">${esc(drive.jobTitle)} · ${esc(drive.packageLpa)} LPA</p>
                <button class="btn ${applied ? 'btn-outline-dark' : 'btn-ch-primary'} btn-sm" ${applied ? 'disabled' : ''}>
                    ${applied ? 'Applied ✓' : 'Apply'}
                </button>
            </div>`;
        container.appendChild(col);

        if (!applied) {
            col.querySelector('button').addEventListener('click', () => apply(drive.id));
        }
    });
}

async function apply(driveId) {
    const res = await fetch(`/api/applications?driveId=${driveId}`, {
        method: 'POST',
        credentials: 'include'
    });
    if (res.ok) {
        showMsg('actionMsg', 'Application submitted.', 'success');
        refresh();
    } else {
        showMsg('actionMsg', await readError(res, 'Could not apply to this drive.'), 'danger');
    }
}

function renderApplications(apps) {
    const container = document.getElementById('applicationsContainer');
    container.innerHTML = '';

    if (apps.length === 0) {
        container.innerHTML = '<div class="ch-empty">You have not applied to any drive yet.</div>';
        return;
    }

    apps.forEach(app => {
        const row = document.createElement('div');
        row.className = 'ch-card mb-3 d-flex justify-content-between align-items-center';
        row.innerHTML = `
            <div>
                <h5 style="font-family:'Fraunces',serif; margin-bottom:0.2rem;">${esc(app.companyName)}</h5>
                <p style="color:#6B7088; margin:0;">${esc(app.jobTitle)} · Applied on ${new Date(app.appliedOn).toLocaleDateString()}</p>
            </div>
            <span class="badge-status badge-${app.status.toLowerCase()}">${esc(app.status)}</span>`;
        container.appendChild(row);
    });
}

init();