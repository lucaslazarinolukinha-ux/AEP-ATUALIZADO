-- RN06: materiais pré-cadastrados (compatível com MySQL e PostgreSQL; não duplica a cada inicialização)
INSERT INTO material (nome, descricao, categoria) SELECT 'Papel', 'Papel, papelão e jornais', 'Papel' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Papel');
INSERT INTO material (nome, descricao, categoria) SELECT 'Plástico', 'Garrafas PET, embalagens e sacolas', 'Plástico' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Plástico');
INSERT INTO material (nome, descricao, categoria) SELECT 'Vidro', 'Garrafas e frascos de vidro', 'Vidro' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Vidro');
INSERT INTO material (nome, descricao, categoria) SELECT 'Metal', 'Latas de alumínio e aço', 'Metal' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Metal');
INSERT INTO material (nome, descricao, categoria) SELECT 'Eletrônicos', 'Aparelhos e componentes eletrônicos', 'Eletrônicos' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Eletrônicos');
INSERT INTO material (nome, descricao, categoria) SELECT 'Pilhas e Baterias', 'Pilhas e baterias usadas', 'Perigosos' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Pilhas e Baterias');
INSERT INTO material (nome, descricao, categoria) SELECT 'Óleo de Cozinha', 'Óleo vegetal usado', 'Orgânicos' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Óleo de Cozinha');
INSERT INTO material (nome, descricao, categoria) SELECT 'Madeira', 'Madeira e móveis descartados', 'Madeira' FROM (SELECT 1) t WHERE NOT EXISTS (SELECT 1 FROM material WHERE nome = 'Madeira');
