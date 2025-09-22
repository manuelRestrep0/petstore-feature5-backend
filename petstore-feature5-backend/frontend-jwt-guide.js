// GUÍA PARA EL FRONTEND - Implementación JWT básica
// Copiar y pegar en el proyecto de frontend

// 1. Función para hacer login
async function loginUser(email, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        const data = await response.json();
        
        if (response.ok && data.token) {
            // Guardar token en localStorage
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('userData', JSON.stringify(data.user));
            
            console.log('Login exitoso:', data);
            return { success: true, data };
        } else {
            console.error('Error en login:', data);
            return { success: false, error: data.message };
        }
    } catch (error) {
        console.error('Error de red:', error);
        return { success: false, error: 'Error de conexión' };
    }
}

// 2. Función para hacer peticiones autenticadas
async function makeAuthenticatedRequest(url, options = {}) {
    const token = localStorage.getItem('authToken');
    
    if (!token) {
        console.error('No hay token de autenticación');
        return { success: false, error: 'No autenticado' };
    }
    
    try {
        const response = await fetch(url, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
                ...options.headers
            }
        });
        
        const data = await response.json();
        
        if (response.status === 401 || response.status === 403) {
            // Token expirado o inválido
            localStorage.removeItem('authToken');
            localStorage.removeItem('userData');
            console.error('Token inválido, redirigir a login');
            return { success: false, error: 'Token expirado' };
        }
        
        return { success: response.ok, data };
    } catch (error) {
        console.error('Error en petición:', error);
        return { success: false, error: 'Error de conexión' };
    }
}

// 3. Función para verificar si está logueado
function isLoggedIn() {
    const token = localStorage.getItem('authToken');
    const userData = localStorage.getItem('userData');
    return !!(token && userData);
}

// 4. Función para obtener datos del usuario
function getUserData() {
    const userData = localStorage.getItem('userData');
    return userData ? JSON.parse(userData) : null;
}

// 5. Función para logout
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userData');
    console.log('Logout exitoso');
    // Redirigir a página de login
}

// 6. Verificar token al cargar la página
async function checkAuthStatus() {
    if (!isLoggedIn()) {
        return false;
    }
    
    // Verificar si el token sigue siendo válido
    const result = await makeAuthenticatedRequest('http://localhost:8080/api/auth/verify');
    
    if (!result.success) {
        logout();
        return false;
    }
    
    return true;
}

// EJEMPLOS DE USO:

// Ejemplo 1: Login
/*
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    const result = await loginUser(email, password);
    
    if (result.success) {
        alert('Login exitoso');
        window.location.href = '/dashboard'; // Redirigir
    } else {
        alert('Error: ' + result.error);
    }
});
*/

// Ejemplo 2: Petición protegida
/*
async function loadUserProfile() {
    const result = await makeAuthenticatedRequest('http://localhost:8080/api/auth/me');
    
    if (result.success) {
        console.log('Perfil del usuario:', result.data);
        // Mostrar datos en la UI
    } else {
        console.error('Error cargando perfil:', result.error);
    }
}
*/

// Ejemplo 3: Verificar autenticación al cargar página
/*
window.addEventListener('DOMContentLoaded', async () => {
    const isAuthenticated = await checkAuthStatus();
    
    if (!isAuthenticated) {
        window.location.href = '/login';
    } else {
        console.log('Usuario autenticado:', getUserData());
    }
});
*/
