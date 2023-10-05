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
- [x] US002 - Criar recurso que permite criar, alterar, consultar e remover produtos.  
  “Toda a estrutura de persistência e consulta -> endpoints, servlets, db”
- [x] US003 - Criar recurso para ativar ou desativar um produto.
- [x] US004 - Criar recurso para consultar somente produtos ativos.  
  • Uma consulta para retornar todos os produtos ativos.  
  • Outra consulta para retornar um produto e somente caso ele esteja ativo.  
- [x] US005 - Criar recurso para consultar somente produtos inativos.  
  • Consulta deverá retornar todos os produtos inativos
- [x] US006 - Criar recurso para consultar todos os produtos em quantidade de estoque menor que o
  estoque mínimo definido.
- [x] US007 - Criar recurso que permita cadastrar produtos em lote.
- [x] US008 - Criar recurso para atualizar o preço de produtos em lote, por valor fixo, aumentar ou diminuir
  um valor e aumentar ou diminuir percentualmente.  
  • Para esta operação, receber Hash do produto, operação a ser realizada e valor.
- [x] US009 - Criar recurso que permita atualizar estoque em lote.  
• Para essa operação, receber Hash do produto e Valor a ser adicionado ou removido do
  estoque.

<h3>💼 Regras de Negócio</h3>

- [x]  RN001 - Produto deve persistir as informações id e hash automáticos.
- [x]  RN002 - Não permitir cadastrar produtos com nomes duplicados.
- [x]  RN003 - Não permitir cadastrar produtos com ean13 duplicados.
- [x]  RN004 - Não permitir cadastrar produtos com preço, quantidade ou estoque_min negativo.
- [x]  RN005 - Não permitir alterar as informações de id, hash, nome, ean13, dtcreate,
  dtupdate pelo usuário
- [x]  RN006 - Um novo produto deve ser cadastrado somente com os campos nome,
  descrição, ean13, preço, quantidade e estoque mínimo.
- [x]  RN007 - Quantidade, estoque mínimo e preço não podem ser nulos, sendo o valor
  padrão zero.
- [x]  RN008 - Nome não pode ser nulo nem vazio.
- [x]  RN009 - dtcreate deve ser preenchido com a timestamp atual, dtupdate preenchido
  com nulo e lativo com falso.
- [x]  RN010 - Será permitido alterar o campo lativo pelo usuário somente na funcionalidade
  específica para tal.
- [x]  RN011 - Sempre que atualizar alguma informação do produto seja através de qualquer
  funcionalidade, atualizar dtupdate com o timestamp atual.
- [x]  RN012 - Não permitir atualizar informações de um produto inativo exceto para caso de
  reativação.
- [x]  RN013 - A API Rest deve receber como parâmetro do usuário somente o UUID para
  fins de interação com os produtos.
- [x] RN014 - Ativar o produto deve-se atualizar o campo lativo com valor true.
- [x] RN015 - Desativando o produto deve-se atualizar o campo lativo com valor false.
- [x] RN016 - Consulta deverá retornar todos os produtos ativos nesta situação.
- [x] RN017 - Consulta de produtos com estoque abaixo do mínimo deve considerar somente produtos
  ativos
- [x] RN018 - Será permitido alterar o campo lativo pelo usuário somente na funcionalidade específica
  para tal.
- [x] RN019 - Sempre que atualizar alguma informação do produto seja através de qualquer
  funcionalidade, atualizar dtupdate com o timestamp atual.
- [x] RN020 - Não permitir atualizar informações de um produto inativo exceto para caso de reativação.
- [x] RN021 - A API Rest deve receber como parâmetro do usuário somente o UUID para fins de interação
  com os produtos.
- [x] RN022 - Para efeito do cadastro de produtos em lote, caso algum deles não atenda às regras de
  negócio anteriores, cadastrar somente os que estão conforme solicitado, retornando para o usuário quais registros não foram possíveis ser cadastrados bem, como uma mensagem indicando qual
  informação está incorreta.
- [x] RN023 - Não permitir que um produto fique com valor negativo.


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
- [x] RNF011 - Enviar os valores da requisição no body.
- [x] RNF012 - Responder as requisições com o resultado da operação no body da resposta

<h2>📬 Acesso ao Postman</h2>
[Postman Workspace](https://www.postman.com/xnatnat/workspace/newgo/overview)


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
