# Bank Slips Rest API

API REST para geração de boletos que será consumida por um módulo de um sistema de gestão financeira de microempresas.

1. [Descrição da API](#Descricao)
    1. [Endpoints](#Endpoints)
    2. [HTTP Status Code](#HttpStatus)
    3. [Recursos](#Recursos)
2. [Executando a API](#Executando)

## Descrição da API<a name="Descricao"></a>
A api conta com endpoints para:
* [Criar boleto](#Criar)
* [Listar boletos](#Listar)
* [Ver detalhes de um boleto](#Detalhes)
* [Pagar um boleto](#Pagar)
* [Cancelar um boleto](#Cancelar)

### Endpoints<a name="Endpoints"></a>
Caminho | Método | Descrição
--- | --- | ---
http://localhost:8080/rest/bankslips               | POST   | Criar boleto
http://localhost:8080/rest/bankslips/              | GET    | Lista de boletos
http://localhost:8080/rest/bankslips/{id}          | GET    | Ver detalhes de um boleto
http://localhost:8080/rest/bankslips/{id}/payments | PUT    | Pagar um boleto
http://localhost:8080/rest/bankslips/{id}          | DELETE | Cancelar um boleto

### HTTP Status Code<a name="HttpStatus"></a>
Código | Erro | Descrição
--- | --- | ---
200 | OK                    | Operação realizada com sucesso.
201 | Created               | Operação realizada, resultando na criação de um novo recurso.
204 | Empty                 | Operação realizada com sucesso, mas com mensagem de resposta sem corpo.
400 | Bad Request           | A mensagem requisição não possuí corpo.
404 | Not Found             | O recurso informado na requisição não foi encontrado.
422 | Unprocessable Entity  | Paramêtros obrigatórios na requisição não foram informados ou foram informados com formato inválido.
500 | Internal Server Error | Erro não esperado no lado do servidor.

### Recursos<a name="Recursos"></a>

#### Criar boleto<a name="Criar"></a>
**Endpoint:** POST http://localhost:8080/rest/bankslips

Esse método deve receber um novo boleto e inseri-lo em um banco de dados para ser consumido
pela própria API. Todos os campos são obrigatórios.

##### Dados de entrada
Campo | Tipo | Formato
--- | --- | ---
due_date       | date       | yyyy-MM-dd
total_in_cents | BigDecimal | valor em centavos
customer       | string     |

**Exemplo de requisição**
```json5
{
    "due_date" : "2018-01-01" ,
    "total_in_cents" : "100000" ,
    "customer" : "Trillian Company"
}
```

##### Dados de saída
Campo | Tipo | Formato
--- | --- | ---
id             | UUID       | UUID
due_date       | date       | yyyy-MM-dd
total_in_cents | BigDecimal | valor em centavos
customer       | string     |
status         | string     | PENDING, PAID, CANCELED

**Exemplo de resposta:**
```json5
{
    "id" : "84e8adbf-1a14-403b-ad73-d78ae19b59bf" ,
    "due_date" : "2018-01-01" ,
    "total_in_cents" : "100000" ,
    "customer" : "Trillian Company" ,
    "status" : "PENDING"
}
```
**HTTP STATUS**
* 201 : Bankslip created
* 400 : Bankslip not provided in the request body
* 422 : Invalid bankslip provided.The possible reasons are:
    * A field of the provided bankslip was null or with invalid values


#### Lista de boletos<a name="Listar"></a>
**Endpoint:** GET http://localhost:8080/rest/bankslips/

Esse método da API retorna uma lista de boletos em formato JSON.

##### Dados de saída
Campo | Tipo | Formato
--- | --- | ---
id             | UUID       | UUID
due_date       | date       | yyyy-MM-dd
total_in_cents | BigDecimal | valor em centavos
customer       | string     |
status         | string     | PENDING, PAID, CANCELED

**Exemplo de resposta:**
```json5
[
    {
        "id" : "84e8adbf-1a14-403b-ad73-d78ae19b59bf" ,
        "due_date" : "2018-01-01" ,
        "total_in_cents" : "100000" ,
        "customer" : "Ford Prefect Company" ,
        "status" : "PENDING"
    },
    {
        "id" : "c2dbd236-3fa5-4ccc-9c12-bd0ae1d6dd89" ,
        "due_date" : "2018-02-01" ,
        "total_in_cents" : "200000" ,
        "customer" : "Zaphod Company" ,
        "status" : "PAID"
    }
]
```

#### Ver detalhes de um boleto<a name="Detalhes"></a>
**Endpoint:** GET http://localhost:8080/rest/bankslips/{id}

Esse método da API retorna um boleto filtrado pelo id. Caso o boleto esteja em atrasado, o valor da multa é calculado.

Regra do cálculo da multa aplicada por dia para os boletos atrasados:
* Até 10 dias: Multa de 0,5% (Juros Simples)
* Acima de 10 dias: Multa de 1% (Juros Simples)

##### Dados de saída
Campo | Tipo | Formato
--- | --- | ---
id                 | UUID       | UUID
due_date           | date       | yyyy-MM-dd
payment_date       | date       | yyyy-MM-dd
total_in_cents     | BigDecimal | valor em centavos
customer           | string     |
fine               | BigDecimal | valor da multa em centavos
status             | string     | PENDING, PAID, CANCELED

**Exemplo de resposta:**
```json5
{
    "id" : "c2dbd236-3fa5-4ccc-9c12-bd0ae1d6dd89" ,
    "due_date" : "2018-05-10" ,
    "payment_date" : "2018-05-13" ,
    "total_in_cents" : "99000" ,
    "customer" : "Ford Prefect Company" ,
    "fine" : "1485" ,
    "status" : "PAID"
}
```

**HTTP STATUS**
* 200 : Ok
* 404 : Bankslip not found with the specified id

#### Pagar um boleto<a name="Pagar"></a>
**Endpoint:** PUT http://localhost:8080/rest/bankslips/{id}/payments

Esse método da API altera o status do boleto para PAID.

Request:
```json5
{
    "payment_date" : "2018-06-30"
}
```
Campo | Tipo | Formato
--- | --- | ---
payment_date | Date | yyyy-MM-dd

**HTTP STATUS**
* 204 : No content
* 404 : Bankslip not found with the specified id

#### Cancelar um boleto<a name="Cancelar"></a>
**Endpoint:** DELETE http://localhost:8080/rest/bankslips/{id}

Esse método da API altera o status do boleto para CANCELED.

**HTTP STATUS**
* 204 : Bankslip canceled
* 404 : Bankslip not found with the specified id

## Executando a API<a name="Executando"></a>

### Pré-requisitos

* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [Docker](https://docs.docker.com/install/)
* [Docker Compose](https://docs.docker.com/compose/install/)
* [Postman](https://www.getpostman.com/docs/v6/postman/launching_postman/installation_and_updates)

### Gerando as imagens docker

Para gerar as imagens docker utilize o shell script "build.sh" disponível na raiz do projeto.

Usando seu terminal de preferência, execute:

```
chmod +x build.sh
./build.sh
```

**Obs:** Todas as imagens docker necessárias estão disponíveis no [Docker Hub](https://hub.docker.com/u/maxsuelmarinho/)
### Executando os containers

Após clonar o repositório, usando seu terminal de preferência, vá para a raiz do projeto e execute o seguinte comando:
```
docker-compose up -d
```

### Testando a API

1. Importe no Postman os arquivos collection e o environment localizados no diretório: <projeto>/postman/. Para saber como importar collection no Postman, consulte: [Exporting and importing Postman data](https://www.getpostman.com/docs/v6/postman/collections/data_formats#exporting-and-importing-postman-data)
    * Caso os container não sejam executados como localhost (quando por exemplo é executado em uma máquina virtual), será necessário alterar a variável "url" nas variáveis de ambiente do Postman, como por exemplo: http://{ip-container}:8080
2. Execute a collection através do Collection Runner. Para saber como executar, consulte: [Intro to collection runs](https://www.getpostman.com/docs/v6/postman/collection_runs/intro_to_collection_runs)
