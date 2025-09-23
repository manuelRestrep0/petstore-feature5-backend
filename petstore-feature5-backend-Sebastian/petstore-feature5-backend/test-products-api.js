// Script de pruebas para la API de productos
// Ejecutar con: node test-products-api.js

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

// Test 1: Verificar estado del servicio de productos
async function testProductServiceStatus() {
    console.log('🔍 Verificando estado del servicio de productos...');
    
    const result = await makeRequest(`${API_BASE}/api/products/status`);
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200) {
        console.log('✅ Servicio de productos funcionando');
    } else {
        console.log('❌ Error en el servicio de productos');
    }
}

// Test 2: Obtener todos los productos
async function testGetAllProducts() {
    console.log('\n📦 Obteniendo todos los productos...');
    
    const result = await makeRequest(`${API_BASE}/api/products`);
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200 && result.data && result.data.success) {
        console.log(`✅ Productos obtenidos: ${result.data.products ? result.data.products.length : 0} productos`);
        if (result.data.products && result.data.products.length > 0) {
            console.log('Primer producto:', result.data.products[0]);
        }
    } else {
        console.log('❌ Error obteniendo productos');
    }
}

// Test 3: Obtener productos por ID de categoría
async function testGetProductsByCategoryId() {
    console.log('\n🏷️ Obteniendo productos por ID de categoría (ID: 1)...');
    
    const result = await makeRequest(`${API_BASE}/api/products/category/1`);
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200 && result.data && result.data.success) {
        console.log(`✅ Productos por categoría obtenidos: ${result.data.products ? result.data.products.length : 0} productos`);
        if (result.data.products && result.data.products.length > 0) {
            console.log('Primer producto de la categoría:', result.data.products[0]);
        }
    } else {
        console.log('❌ Error obteniendo productos por categoría');
    }
}

// Test 4: Obtener productos por nombre de categoría
async function testGetProductsByCategoryName() {
    console.log('\n🏷️ Obteniendo productos por nombre de categoría (Electrodomésticos)...');
    
    const result = await makeRequest(`${API_BASE}/api/products/category/name/Electrodomésticos`);
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 200 && result.data && result.data.success) {
        console.log(`✅ Productos por nombre de categoría obtenidos: ${result.data.products ? result.data.products.length : 0} productos`);
        if (result.data.products && result.data.products.length > 0) {
            console.log('Primer producto de la categoría:', result.data.products[0]);
        }
    } else {
        console.log('❌ Error obteniendo productos por nombre de categoría');
    }
}

// Test 5: Probar con categoría inexistente
async function testGetProductsByNonExistentCategory() {
    console.log('\n🚫 Probando con categoría inexistente...');
    
    const result = await makeRequest(`${API_BASE}/api/products/category/name/CategoriaInexistente`);
    
    console.log('Status:', result.status);
    console.log('Response:', result.data);
    
    if (result.status === 500 && result.data && !result.data.success) {
        console.log('✅ Correctamente maneja categoría inexistente');
    } else {
        console.log('❌ No maneja correctamente categoría inexistente');
    }
}

// Ejecutar todas las pruebas
async function runAllTests() {
    console.log('🚀 Iniciando pruebas de la API de productos\n');
    
    await testProductServiceStatus();
    await testGetAllProducts();
    await testGetProductsByCategoryId();
    await testGetProductsByCategoryName();
    await testGetProductsByNonExistentCategory();
    
    console.log('\n✨ Pruebas de productos completadas');
}

// Ejecutar si es llamado directamente
if (typeof window === 'undefined') {
    // Node.js
    const fetch = require('node-fetch');
    runAllTests();
} else {
    // Browser - exponer funciones globalmente
    window.testProductsAPI = {
        runAllTests,
        testProductServiceStatus,
        testGetAllProducts,
        testGetProductsByCategoryId,
        testGetProductsByCategoryName,
        testGetProductsByNonExistentCategory
    };
}
