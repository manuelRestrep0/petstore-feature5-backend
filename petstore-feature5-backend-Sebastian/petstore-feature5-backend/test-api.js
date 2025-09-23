// Script de pruebas para la API de autenticación
// Ejecutar con: node test-api.js

const API_BASE = 'http://localhost:8080';

// Función para hacer peticiones HTTP
async function makeRequest(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        const data = await response.json();
        return { status: response.status, data };
    } catch (error) {
        return { error: error.message };
    }
}

// Test 1: Login con credenciales correctas
async function testLogin() {
    console.log('🔐 Probando login...');
    
    const loginData = {
        email: 'admin@marketing.com',
        password: 'admin123'
    };
    
    const result = await makeRequest(`${API_BASE}/api/auth/login`, {
        method: 'POST',
        body: JSON.stringify(loginData)
    });
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.data && result.data.token) {
        console.log('✅ Login exitoso - Token recibido');
        return result.data.token;
    } else {
        console.log('❌ Login falló');
        return null;
    }
}

// Test 2: Verificar token
async function testVerifyToken(token) {
    console.log('\n🔍 Verificando token...');
    
    const result = await makeRequest(`${API_BASE}/api/auth/verify`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200) {
        console.log('✅ Token válido');
    } else {
        console.log('❌ Token inválido');
    }
}

// Test 3: Obtener información del usuario
async function testGetUserInfo(token) {
    console.log('\n👤 Obteniendo info del usuario...');
    
    const result = await makeRequest(`${API_BASE}/api/auth/me`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200) {
        console.log('✅ Información obtenida correctamente');
    } else {
        console.log('❌ Error obteniendo información');
    }
}

// Test 4: Probar sin token (debe fallar)
async function testWithoutToken() {
    console.log('\n🚫 Probando endpoint protegido sin token...');
    
    const result = await makeRequest(`${API_BASE}/api/auth/me`, {
        method: 'GET'
    });
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 401 || result.status === 403) {
        console.log('✅ Correctamente bloqueado sin token');
    } else {
        console.log('❌ ERROR: Permitió acceso sin token');
    }
}

// Ejecutar todas las pruebas
async function runAllTests() {
    console.log('🚀 Iniciando pruebas de la API\n');
    
    // Test login
    const token = await testLogin();
    
    if (token) {
        // Tests con token válido
        await testVerifyToken(token);
        await testGetUserInfo(token);
    }
    
    // Test sin token
    await testWithoutToken();
    
    console.log('\n✨ Pruebas completadas');
}

// Ejecutar si es llamado directamente
if (typeof window === 'undefined') {
    // Node.js
    const fetch = require('node-fetch');
    runAllTests();
} else {
    // Browser - exponer funciones globalmente
    window.testAPI = {
        runAllTests,
        testLogin,
        testVerifyToken,
        testGetUserInfo,
        testWithoutToken
    };
}
