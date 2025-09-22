-- Script para insertar datos de prueba en Supabase
-- Ejecutar estos comandos en el SQL Editor de Supabase

-- 1. Insertar roles
INSERT INTO public.role (role_name) VALUES 
    ('Marketing Admin'),
    ('User'),
    ('Viewer')
ON CONFLICT (role_name) DO NOTHING;

-- 2. Insertar status
INSERT INTO public.status (status_name) VALUES 
    ('Active'),
    ('Inactive'),
    ('Pending'),
    ('Expired')
ON CONFLICT (status_name) DO NOTHING;

-- 3. Insertar categorías
INSERT INTO public.category (category_name, description) VALUES 
    ('Dogs', 'Products for dogs'),
    ('Cats', 'Products for cats'),
    ('Birds', 'Products for birds'),
    ('Fish', 'Products for fish'),
    ('Small Animals', 'Products for small animals like rabbits, hamsters')
ON CONFLICT (category_name) DO NOTHING;

-- 4. Insertar usuario Marketing Admin de prueba
-- NOTA: En producción la contraseña debe estar hasheada con BCrypt
INSERT INTO public.user (user_name, email, password, role_id) 
VALUES (
    'Admin Marketing', 
    'admin@petstore.com', 
    '$2a$10$K8k7yX9kF3WzE3Z5A1B2H.Xy5Z8fN9mF5P3Q7J2N5M8K3F7G2H4L6', -- password: admin123
    (SELECT role_id FROM public.role WHERE role_name = 'Marketing Admin')
)
ON CONFLICT (email) DO NOTHING;

-- 5. Insertar algunos productos de ejemplo
INSERT INTO public.product (product_name, base_price, sku, category_id) VALUES 
    ('Premium Dog Food', 45.99, 10001, (SELECT category_id FROM public.category WHERE category_name = 'Dogs')),
    ('Cat Litter Box', 29.99, 10002, (SELECT category_id FROM public.category WHERE category_name = 'Cats')),
    ('Bird Cage', 89.99, 10003, (SELECT category_id FROM public.category WHERE category_name = 'Birds')),
    ('Fish Tank Filter', 25.99, 10004, (SELECT category_id FROM public.category WHERE category_name = 'Fish')),
    ('Rabbit Food', 15.99, 10005, (SELECT category_id FROM public.category WHERE category_name = 'Small Animals'))
ON CONFLICT (sku) DO NOTHING;

-- 6. Insertar algunas promociones de ejemplo
INSERT INTO public.promotion (promotion_name, description, start_date, end_date, discount_value, status_id, user_id, category_id) 
VALUES 
    (
        'Dog Food Special', 
        '20% off all premium dog food', 
        CURRENT_DATE, 
        CURRENT_DATE + INTERVAL '30 days', 
        20.0, 
        (SELECT status_id FROM public.status WHERE status_name = 'Active'),
        (SELECT user_id FROM public.user WHERE email = 'admin@petstore.com'),
        (SELECT category_id FROM public.category WHERE category_name = 'Dogs')
    ),
    (
        'Cat Products Sale', 
        '15% off all cat products', 
        CURRENT_DATE + INTERVAL '5 days', 
        CURRENT_DATE + INTERVAL '35 days', 
        15.0, 
        (SELECT status_id FROM public.status WHERE status_name = 'Pending'),
        (SELECT user_id FROM public.user WHERE email = 'admin@petstore.com'),
        (SELECT category_id FROM public.category WHERE category_name = 'Cats')
    );

-- Verificar los datos insertados
SELECT 'Roles:' as table_name, role_name as name FROM public.role
UNION ALL
SELECT 'Status:', status_name FROM public.status
UNION ALL  
SELECT 'Categories:', category_name FROM public.category
UNION ALL
SELECT 'Users:', user_name FROM public.user
UNION ALL
SELECT 'Products:', product_name FROM public.product
UNION ALL
SELECT 'Promotions:', promotion_name FROM public.promotion;
