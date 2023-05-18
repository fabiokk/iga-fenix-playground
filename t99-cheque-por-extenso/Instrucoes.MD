Desafio Cheque por Extenso:

Apesar de o volume de cheques emitidos ter diminuído drasticamente nos
últimos anos, principalmente devido a popularização dos cartões de crédito
e débito, eles ainda são utilizados em muitas compras, especialmente as de
valor alto. E para auxiliar no seu preenchimento, vários estabelecimentos
possuem máquinas que dado o valor da compra, preenchem o cheque com o seu
valor por extenso.

Exemplo:
0.05 -> cinco centavos
2.25 -> dois reais e vinte e cinco centavos
15.16 -> quinze reais e dezesseis centavos
10 -> dez reais
99.30 -> noventa e nove reais e trita centavos
"""

Escreva um endoint (POST) que receba através dois parametros via body: moeda, que pode ser real ou dolar e valor, que é um float maior que 0.00 através do path "/cheque" e retorne:
    status code 403 / mensagem "O número deve ser positivo" caso o valor seja menor ou igual a 0.00 ou a moeda não estiver na lista de moedas aceitas
    para respostas válidas, o status code deve ser 201 e a resposta deve ser um json seguindo o padrão abaixo:
        {
            "message": "cheque processado com sucesso!",
            "detail": {
                "valor": {{ valor recebido}},
                "moeda":  {{ moeda recebida }},
                "resposta": {{ valor do cheque por extenso }}
            }
        }

Para este desafio, o valor máximo aceito é 99.99

Valores que devem ser obrigatoriamente testados:
    [0.0, 0.01, "0.05", 0.10, 0.11, 0.21, 0.50, 0.99, 1, 5, 1.01, 1.05, 1.25, 1.99, 10.0, 15.0, 19.0, 20.2, 21.0, 85.0, 99.0, 99.05, 99.99]


ordem dos participantes 

[
    Rafael,
    Eli,
    Kuba,
    Luana,
    Anderson,
    Jonathan
]