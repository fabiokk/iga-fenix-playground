Desafio Romeu e Julieta:

Escreva um endoint (get) que receba um número inteiro através do path "/romeuejulieta/{numero}" e retorne:
    status code 403 / mensagem "O número deve ser positivo" caso o numero seja menor ou igual a 0 
    para respostas válidas, o status code deve ser 200 e a resposta varia de acordo com o número informado:
        mensagem "queijo" se o número recebido for divisível por 3
        mensagem "goiabada" se o número recebido for divisível por 5
        mensagem "romeu e julieta" se o número recebido for divisível por 3 e 5
        Para qualquer outro número, o próprio número deve ser retornado.