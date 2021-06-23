# Avaliação Java


## Descrição

  O teste consiste em construir a camada de serviço de um pseudo ecommerce de games mobile utilizando Java

## Como executar os testes
  
  O projeto usa o maven wrapper (mvnw).
  Para executar os testes de exemplo basta o comando abaixo:
  ```sh
  ./mvnw clean test
  ```

## Requisitos

  - Existe um exemplo de carga de banco de dados em memória em [ProductDaoExampleTest.java](./src/test/java/br/com/supera/game/store/ProductDaoExampleTest.java)
  - Os valores exibidos no checkout (frete, subtotal e total) devem ser calculados dinamicamente
  - O usuário poderá adicionar e remover produtos do carrinho
  - O usuário poderá ordenar os produtos por preço, popularidade (score) e ordem alfabética.
  - A cada produto adicionado, deve-se somar R$ 10,00 ao frete.
  - Quando o valor dos produtos adicionados ao carrinho for igual ou superior a R$ 250,00, o frete é grátis.

## O que iremos avaliar

Levaremos em conta os seguintes critérios:

  - Cumprimento dos requisitos
  - Qualidade do projeto de API e fluidez da DX
  - Organização do código e boas práticas
  - Domínio das linguagens, bibliotecas e ferramentas utilizadas
  - Organização dos commits
  - Escrita e cobertura de testes

## Sobre a entrega

  - A API pode ser HTTP, Restful, WSDL/SOAP, HATEOAS ou gRCP mas deverá ser documentado no [README.md](./README.md) como executar/compilar/empacotar o projeto e quais os endpoints solicitados nos requisitos acima. 
  - A versão do Java pode ser atualizada para 11 ou 14.
  - Não existe restrição de framework (EE, Spring, Quarkus etc) mas será melhor avaliado se justificado no [README.md](./README.md) os motivos da decisão.

## Entrega

### Rotas

#### /add

  - GET: Adiciona um novo produto ao carrinho e retorna o próprio carrinho
    - Param: ```productId```
  
#### /remove

  - GET: Remove um ou todos os itens do carrinho e retorna o próprio carrinho
    -Param: ```productId```, ```all```

#### /sort 

  - GET: Ordena o carrinho ou todos os produto por nome(padrão), preço e popularidade de forma crescente(padrão) ou decrescente
    -Param: ```sortMethod```, ```order```, ```productIds```,

### Empacotar

  - Para empacotar o projeto usando docker: 
  
  ```
    make build
  ```

  - Para empacotar esse projeto usando maven nativo:

  ```
    mvn package
  ```

### Executar

  - Para executar o projeto usando docker:

  ```
    make run
  ```

  - Para executar esse projeto usando maven nativo:

  ```
    java -jar target/game-store-1.0.0-SNAPSHOT.jar
  ```

### Explicações da Entrega

  - Eu usei spring pois é a biblioteca mais conhecida possuindo bons tutorias e documentações

  - Está entrega não contempla tudo que foi solicitado, por falta de tempo e conhecimento. O que acabou faltando foi os testes e o sistema de checkout, porém há uma branch chamada [feat/checkout](https://github.com/MikaelSaraiva/PS-Java/tree/feat/checkout) que possui minha tentativa de implemtação do checkout.