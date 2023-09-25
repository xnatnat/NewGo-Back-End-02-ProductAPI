# NewGo Back-End 02 – ProductAPI

<h2>🎯 Objetivo</h2>
<p>Implementar uma <strong>API REST</strong> para realizar cadastro de produtos e
controle de estoque e preço destes produtos.</p>

<h2>💬 Legendas</h2>
<ul>
    <li>US - User Story.</li>
    <li>RN - Regras de Negócio.</li>
    <li>RNF - Requisitos Não Funcionais</li>
</ul>

<h2>
🛑 Pré-requistos
</h2>

<h3>🗣️ User Story</h3>

- [x] US001 - Criar tabela para armazenar informação de produtos com os seguintes
  campos:  
  id: long  
  hash: UUID  
  nome: varchar(255)  
  descricao: text  
  ean13: varchar(13)  
  preco: numeric(13,2)  
  quantidade: numeric(13,2)  
  estoque_min: numeric(13,2)  
  dtcreate: timestamp  
  dtupdate: timestamp  
  lativo: boolean
- [ ] US002 - Criar recurso que permite criar, alterar, consultar e remover produtos.  
  “Toda a estrutura de persistência e consulta -> endpoints, servlets, db”

<h3>💼 Regras de Negócio</h3>

- [x]  RN001 - Produto deve persistir as informações id e hash automáticos.
- [x]  RN002 - Não permitir cadastrar produtos com nomes duplicados.
- [x]  RN003 - Não permitir cadastrar produtos com ean13 duplicados.
- [x]  RN004 - Não permitir cadastrar produtos com preço, quantidade ou estoque_min negativo.
- [ ]  RN005 - Não permitir alterar as informações de id, hash, nome, ean13, dtcreate,
  dtupdate pelo usuário
- [x]  RN006 - Um novo produto deve ser cadastrado somente com os campos nome,
  descrição, ean13, preço, quantidade e estoque mínimo.
- [x]  RN007 - Quantidade, estoque mínimo e preço não podem ser nulos, sendo o valor
  padrão zero.
- [x]  RN008 - Nome não pode ser nulo nem vazio.
- [x]  RN009 - dtcreate deve ser preenchido com a timestamp atual, dtupdate preenchido
  com nulo e lativo com falso.
- [ ]  RN010 - Será permitido alterar o campo lativo pelo usuário somente na funcionalidade
  específica para tal.
- [ ]  RN011 - Sempre que atualizar alguma informação do produto seja através de qualquer
  funcionalidade, atualizar dtupdate com o timestamp atual.
- [ ]  RN012 - Não permitir atualizar informações de um produto inativo exceto para caso de
  reativação.
- [x]  RN013 - A API Rest deve receber como parâmetro do usuário somente o UUID para
  fins de interação com os produtos.

<h3>📋 Requisitos Não Funcionais</h3>

- [x] RNF001 - Utilizar Banco de dados Postgres;
- [x] RNF002 - Utilizar servlets para receber as requisições HTTP;
- [x] RNF003 - Versão do java 8;
- [x] RNF005 - Não deve ser utilizado nenhum framework para criação da API bem como
  para persistência dos dados;
- [x] RNF004* - Utilizar arquitetura de 3 camadas;
- [x] RNF006* - Utilizar processo gitflow;
- [x] RNF007 - Utilizar uma branch para cada US;
- [x] RNF008 - Utilizar os seguinte prefixos para criação das branchs de acordo com o
  propósito da mesma  
  • bugfix/  
  • feature/  
  • hotfix/  
  • improvement/
- [x] RNF009 - Utilizar o seguinte prefixo para cada commit de acordo com a alteração
  realizada:  
  • feat: (new feature for the user, not a new feature for build script)  
  • fix: (bug fix for the user, not a fix to a build script)  
  • docs: (changes to the documentation)  
  • style: (formatting, missing semicolons, etc; no production code change)  
  • refactor: (refactoring production code, eg. renaming a variable)  
  • test: (adding missing tests, refactoring tests; no production code change)  
  • chore: (updating grunt tasks etc; no production code change)
- [x] RNF011 - Não remover as branches mergeadas para a Main.
- [x] RNF010 - Criar uma coleção Postman para todos os endpoints.
- [x] RNF011 – Modelo de nome do projeto no git: NewGo Back-End 02 – [Nome]


<h2>🛠️ Tecnologias Utilizadas</h2>

<ul>
    <li>IDE IntelliJ</li>
    <li>Java 8</li>
    <li>Maven</li>
    <li><strong>PostgreSQL</strong></li>
    <li>Postman</li>
</ul>


------------

Feito com ♥ por [xnatnat](https://www.linkedin.com/in/xnatnat/ "xnatnat").
