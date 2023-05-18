#!/bin/bash
set -e

function baixarRunDoBinarios() {
  echo "Realizando o update do script run.sh."
  LINK=$1
  set +e
  CODE=$(curl -w %{http_code} ${OPTION_PROXY} -u ${USER_BINARIOS}:${PASSWORD_BINARIOS} -k "${LINK}" -o ./run/run.sh | cat)
  ERRO=$?
  set -e
  if [[ "$CODE" =~ ^2 ]]; then
    echo "Script run.sh baixado do binarios"
  else
    echo "Não foi possivel baixar o run.sh, retornou status code: $CODE."
    echo "O comando curl retornou o erro: $ERRO . Erros do curl: https://curl.se/libcurl/c/libcurl-errors.html"
    echo "Tente baixar manualmente do endereço $1 e coloque dentro da pasta run do projeto."
    exit 1
  fi
}

function atribuirComoExecutavel() {
    chmod +x "$BASE_DIR./run/run.sh"
}

function removerRunShCorrompido() {
    RUN_CORROMPIDO=`find . -name "run.sh" -size -1k | grep /run.sh | wc -l`
    if [[ ${RUN_CORROMPIDO} -ge 1 ]]; then
      printf "\n"
      printf "Excluindo run.sh corrompido. Possivelmente voce nao esta conseguindo acessar o\n"
      printf " repositorio de binarios. Realize a copia manual do link abaixo para a pasta /run \n"
      echo $1
      find . -name "run.sh" -size -1k -delete
      printf "\n"
    fi
}

function removerDevJavaConfigCorrompido() {
    DEV_CONFIG_CORROMPIDO=`find . -name "dev-java-config.jar" -size -1k | grep /dev-java-config.jar | wc -l`
    if [[ ${DEV_CONFIG_CORROMPIDO} -ge 1 ]]; then
      printf "\n"
      printf "Excluindo dev-java-config.jar corrompido,. Possivelmente voce nao esta conseguindo acessar o\n"
      printf " repositorio de binarios. Realize a copia manual do link abaixo para a pasta /run \n"
      echo $1
      find . -name "dev-java-config.jar" -size -1k -delete
      printf "\n"
    fi
}

function baixarDevJavaConfig() {
  echo "Realizando o update do dev-java-config.jar."
  LINK=$1
  set +e
  CODE=$(curl -w %{http_code} ${OPTION_PROXY} -u ${USER_BINARIOS}:${PASSWORD_BINARIOS} -k "${LINK}" -o ./run/dev-java-config.jar | cat)
  ERRO=$?
  set -e
  if [[ "$CODE" =~ ^2 ]]; then
    echo "dev-java-config.jar baixado do binarios"
  else
    echo "Não foi possivel baixar o dev-java-config.jar, retornou status code: $CODE."
    echo "O comando curl retornou o erro: $ERRO . Erros do curl: https://curl.se/libcurl/c/libcurl-errors.html"
    echo "Tente baixar manualmente do endereço $1 e coloque dentro da pasta run do projeto."
    exit 1
  fi
}

function solicitaCredenciais() {
  echo "Informe sua matrícula de usuário:"
  read -r -p "Chave: " USER_BINARIOS
  if [[ "${USER_BINARIOS}" -gt 8 ]]; then
    textoErro "Por favor informe a matricula com até 8 caracteres."
    exit 1
  fi
  read -r -sp "Senha do SisBB: " PASSWORD_BINARIOS
  if [[ -z "$PASSWORD_BINARIOS" ]]; then
    textoErro "Por favor informe a senha."
    exit 1
  fi
  echo -e ''
  if [[ "${USER_BINARIOS}" =~ ^[c,C] ]]; then
    read -r -p "Endereço do proxy da empresa no formato 'http://<ip>:<porta>':" PROXY_EMPRESA
    OPTION_PROXY=" -x $PROXY_EMPRESA" 
  fi
  echo
}

printf "===========================================================================================\n"
printf "============== INICIANDO SCRIPT PARA ATUALIZAÇÃO DO RUN-SH E DEV-JAVA-CONFIG ==============\n"
printf "===========================================================================================\n"

RUN_SH_LINK='https://binarios.intranet.bb.com.br:443/artifactory/generic-bb-binarios-dev-local/dev/scripts/run.sh'
DEV_CONFIG_LINK='https://binarios.intranet.bb.com.br:443/artifactory/generic-bb-binarios-dev-local/dev/dev-java-config/dev-java-config.jar'
OPTION_PROXY=""

solicitaCredenciais
removerRunShCorrompido $RUN_SH_LINK
removerDevJavaConfigCorrompido $DEV_CONFIG_LINK
baixarRunDoBinarios $RUN_SH_LINK
baixarDevJavaConfig $DEV_CONFIG_LINK
atribuirComoExecutavel
removerRunShCorrompido $RUN_SH_LINK
