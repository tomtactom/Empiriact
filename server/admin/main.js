const state = {
  initialized: false,
};

const ui = {
  adminMain: document.getElementById('adminMain'),
  statusHint: document.getElementById('statusHint'),
  loginForm: document.getElementById('loginForm'),
  loginButton: document.getElementById('loginButton'),
  loginMessage: document.getElementById('loginMessage'),
  initModal: document.getElementById('initModal'),
  initForm: document.getElementById('initForm'),
  initPassword: document.getElementById('initPassword'),
  initPasswordRepeat: document.getElementById('initPasswordRepeat'),
  initSaveButton: document.getElementById('initSaveButton'),
  initMessage: document.getElementById('initMessage'),
};

function setMessage(element, text, isError = false) {
  element.textContent = text;
  element.classList.toggle('is-error', isError);
}

function setMainEnabled(enabled) {
  ui.adminMain.classList.toggle('is-disabled', !enabled);
  ui.adminMain.setAttribute('aria-busy', enabled ? 'false' : 'true');
  ui.loginButton.disabled = !enabled;
}

function showInitModal(show) {
  ui.initModal.hidden = !show;
  if (show) {
    ui.initPassword.focus();
  }
}

function validateInitPasswords() {
  const password = ui.initPassword.value.trim();
  const repeat = ui.initPasswordRepeat.value.trim();

  if (password.length < 8) {
    return { valid: false, message: 'Das Passwort muss mindestens 8 Zeichen haben.' };
  }

  if (password !== repeat) {
    return { valid: false, message: 'Die Passwörter stimmen noch nicht überein.' };
  }

  return { valid: true, message: '' };
}

function mapServerError(errorMessage) {
  if (!errorMessage) {
    return 'Es ist ein unerwarteter Fehler aufgetreten. Bitte versuche es erneut.';
  }

  if (errorMessage.includes('mindestens 8 Zeichen')) {
    return 'Passwort zu kurz: Bitte nutze mindestens 8 Zeichen.';
  }

  return `Serverfehler: ${errorMessage}`;
}

async function loadInitializationStatus() {
  setMainEnabled(false);
  setMessage(ui.loginMessage, '');

  try {
    const response = await fetch('../api/auth_status.php', { method: 'GET' });
    const payload = await response.json();

    if (!response.ok || !payload.success) {
      throw new Error(payload.error || 'Initialisierungsstatus konnte nicht geladen werden.');
    }

    state.initialized = !!payload.initialized;

    if (state.initialized) {
      showInitModal(false);
      setMainEnabled(true);
      ui.statusHint.textContent = 'Admin-Zugang ist eingerichtet. Du kannst dich anmelden.';
      return;
    }

    ui.statusHint.textContent = 'Admin-Zugang ist noch nicht eingerichtet.';
    setMessage(ui.initMessage, 'Du richtest jetzt einmalig den Admin-Zugang ein.');
    showInitModal(true);
  } catch (error) {
    showInitModal(false);
    setMessage(ui.statusHint, 'Status konnte nicht geladen werden. Bitte Seite neu laden.', true);
    setMessage(ui.loginMessage, `Fehler: ${error.message}`, true);
  }
}

async function initializeAdmin(password) {
  const response = await fetch('../api/login.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ password }),
  });

  const payload = await response.json();
  if (!response.ok || !payload.success) {
    throw new Error(mapServerError(payload.error));
  }

  if (!payload.initialized) {
    throw new Error('Initialisierung wurde nicht bestätigt. Bitte versuche es erneut.');
  }

  return payload;
}

ui.initForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const validation = validateInitPasswords();

  if (!validation.valid) {
    setMessage(ui.initMessage, validation.message, true);
    return;
  }

  ui.initSaveButton.disabled = true;
  setMessage(ui.initMessage, 'Speichere Passwort …');

  try {
    const payload = await initializeAdmin(ui.initPassword.value);
    setMessage(ui.initMessage, payload.message || 'Initialisierung erfolgreich.');
    showInitModal(false);
    state.initialized = true;
    setMainEnabled(true);
    ui.statusHint.textContent = 'Admin-Zugang ist eingerichtet. Du kannst dich anmelden.';
    setMessage(ui.loginMessage, 'Initialisierung abgeschlossen. Bitte jetzt normal anmelden.');
    ui.initForm.reset();
  } catch (error) {
    setMessage(ui.initMessage, error.message, true);
  } finally {
    ui.initSaveButton.disabled = false;
  }
});

ui.loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();

  if (!state.initialized) {
    showInitModal(true);
    setMessage(ui.loginMessage, 'Bitte richte zuerst den Admin-Zugang ein.', true);
    return;
  }

  const password = document.getElementById('loginPassword').value;

  try {
    ui.loginButton.disabled = true;
    setMessage(ui.loginMessage, 'Prüfe Login …');

    const response = await fetch('../api/login.php', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ password }),
    });

    const payload = await response.json();

    if (!response.ok || !payload.success) {
      throw new Error(payload.error || 'Login fehlgeschlagen.');
    }

    setMessage(ui.loginMessage, 'Login erfolgreich. Admin-Session kann gestartet werden.');
  } catch (error) {
    setMessage(ui.loginMessage, mapServerError(error.message), true);
  } finally {
    ui.loginButton.disabled = false;
  }
});

loadInitializationStatus();
