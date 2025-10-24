// Calendar functionality for visite/monthly.html
(function(){
    const calendarEl = document.getElementById('calendar');
    const modal = document.getElementById('modal');
    const form = document.getElementById('visiteForm');
    const cancelBtn = document.getElementById('modalCancel');
    const formMessage = document.getElementById('formMessage');
    const openAddVisit = document.getElementById('openAddVisit');

    // Modal de détails
    const visiteDetailModal = document.getElementById('visiteDetailModal');
    const visiteDetailBody = document.getElementById('visiteDetailBody');
    const closeDetailModal = document.getElementById('closeDetailModal');
    const closeDetailModalBtn = document.getElementById('closeDetailModalBtn');

    // Store pour les données complètes des visites
    let visitesData = [];

    // helper to get CSRF token rendered by Thymeleaf
    function getCsrf() {
        const tokenInput = form.querySelector('input[name="_csrf"]');
        if (tokenInput) return { name: tokenInput.getAttribute('name'), value: tokenInput.value };
        return null;
    }

    // Fonction pour formater une date
    function formatDate(dateStr) {
        if (!dateStr) return 'Non définie';
        const d = new Date(dateStr);
        return d.toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
    }

    // Fonction pour afficher les détails d'une visite
    function showVisiteDetails(visiteId) {
        const visite = visitesData.find(v => v.id === parseInt(visiteId));
        if (!visite) {
            visiteDetailBody.innerHTML = '<p style="color:red;">Visite introuvable</p>';
            visiteDetailModal.style.display = 'block';
            return;
        }

        let formatBadge = '';
        if (visite.format) {
            const formatLower = visite.format.toLowerCase();
            let badgeClass = 'visite-badge';
            if (formatLower.includes('présentiel') || formatLower.includes('presentiel')) {
                badgeClass += ' badge-presentiel';
            } else if (formatLower.includes('distance')) {
                badgeClass += ' badge-distance';
            } else if (formatLower.includes('hybride')) {
                badgeClass += ' badge-hybride';
            }
            formatBadge = `<span class="${badgeClass}">${visite.format}</span>`;
        } else {
            formatBadge = '<span style="color:#999;">Non spécifié</span>';
        }

        const apprentiNom = visite.apprenti ?
            `${visite.apprenti.prenom || ''} ${visite.apprenti.nom || ''}`.trim() :
            'Non défini';

        const apprentiEmail = visite.apprenti?.email || 'Non renseigné';
        const apprentiTel = visite.apprenti?.telephone || 'Non renseigné';
        const entreprise = visite.apprenti?.entreprise?.raisonSociale || 'Non renseignée';

        visiteDetailBody.innerHTML = `
            <div class="visite-info-row">
                <span class="visite-info-label">Apprenti :</span>
                <span class="visite-info-value"><strong>${apprentiNom}</strong></span>
            </div>
            <div class="visite-info-row">
                <span class="visite-info-label">Email :</span>
                <span class="visite-info-value">${apprentiEmail}</span>
            </div>
            <div class="visite-info-row">
                <span class="visite-info-label">Téléphone :</span>
                <span class="visite-info-value">${apprentiTel}</span>
            </div>
            <div class="visite-info-row">
                <span class="visite-info-label">Entreprise :</span>
                <span class="visite-info-value">${entreprise}</span>
            </div>
            <div class="visite-info-row">
                <span class="visite-info-label">Date :</span>
                <span class="visite-info-value">${formatDate(visite.dateVisite)}</span>
            </div>
            <div class="visite-info-row">
                <span class="visite-info-label">Format :</span>
                <span class="visite-info-value">${formatBadge}</span>
            </div>
            ${visite.commentaires ? `
            <div class="visite-info-row" style="flex-direction:column; gap:4px;">
                <span class="visite-info-label">Commentaires :</span>
                <span class="visite-info-value" style="padding:8px; background:#f5f5f5; border-radius:4px;">${visite.commentaires}</span>
            </div>
            ` : ''}
        `;

        visiteDetailModal.style.display = 'block';
    }

    closeDetailModal.addEventListener('click', () => visiteDetailModal.style.display = 'none');
    closeDetailModalBtn.addEventListener('click', () => visiteDetailModal.style.display = 'none');

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'fr',
        firstDay: 1,
        dayHeaderContent: function(arg) {
            const names = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
            return names[arg.date.getUTCDay()];
        },
        headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth,dayGridWeek' },
        buttonText: { today: "Aujourd'hui", month: 'Mois', week: 'Semaine', day: 'Jour' },
        events: function(fetchInfo, successCallback, failureCallback) {
            fetch('/api/visites')
                .then(r => r.json())
                .then(data => {
                    visitesData = data;
                    const events = data.map(v => ({
                        id: v.id,
                        title: (v.apprenti ? (v.apprenti.prenom ? (v.apprenti.prenom + ' ' + v.apprenti.nom) : v.apprenti.nom) : 'Visite') + (v.format ? ' ('+v.format+')' : ''),
                        start: v.dateVisite,
                        allDay: true
                    }));
                    successCallback(events);
                }).catch(e => { console.error(e); failureCallback(e); });
        },
        dateClick: function(info) {
            // Ne rien faire au clic sur un jour
        },
        eventClick: function(info) {
            showVisiteDetails(info.event.id);
        }
    });

    calendar.render();

    openAddVisit && openAddVisit.addEventListener('click', function(){
        form.reset();
        const today = new Date();
        const iso = today.toISOString().slice(0,10);
        document.getElementById('dateVisite').value = iso;
        modal.style.display = 'block';
        formMessage.style.display = 'none';
    });

    cancelBtn.addEventListener('click', ()=> modal.style.display = 'none');

    form.addEventListener('submit', function(e){
        e.preventDefault();
        const data = {
            apprentiId: Number(document.getElementById('apprentiId').value),
            dateVisite: document.getElementById('dateVisite').value,
            format: document.getElementById('format').value,
            commentaires: document.getElementById('commentaires').value
        };
        const csrf = getCsrf();
        const headers = { 'Content-Type': 'application/json' };
        if (csrf) headers['X-CSRF-TOKEN'] = csrf.value;

        fetch('/api/visites/simple', { method: 'POST', headers, body: JSON.stringify(data) })
            .then(resp => {
                if (!resp.ok) return resp.text().then(t => Promise.reject(t));
                return resp.json();
            })
            .then(created => {
                formMessage.style.display = 'block';
                formMessage.style.color = 'green';
                formMessage.textContent = 'Visite créée.';
                calendar.refetchEvents();
                setTimeout(()=>{ modal.style.display = 'none'; }, 700);
            }).catch(err => {
                formMessage.style.display = 'block';
                formMessage.style.color = 'red';
                formMessage.textContent = 'Erreur: ' + err;
            });
    });

})();

