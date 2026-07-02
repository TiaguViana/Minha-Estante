#  Minha Estante 

Um aplicativo completo para gerenciamento de leitura, permitindo que os usuários busquem livros, organizem suas estantes e protejam suas leituras privadas com segurança biométrica. 

Este projeto utiliza uma arquitetura de **Monorepo**, contendo o aplicativo principal (Android Nativo) e um painel administrativo (React Native) no mesmo repositório.

##  Escopo e Funcionalidades Atendidas

Este projeto atende aos seguintes requisitos técnicos:

* **CRUD no Backend:** Salvamento, listagem, atualização e exclusão de dados utilizando **Firebase Firestore**.
* **Consumo de API REST Externa:** Integração com a **Google Books API** para busca de títulos, autores e capas.
* **Segurança Biométrica:** Acesso à "Estante Secreta" restrito por autenticação de digital/Face ID nativa do sistema.
* **Cliente Cross-Platform:** Painel administrativo desenvolvido em **React Native / Expo**.
* **Acessibilidade:** Suporte completo a leitores de tela (`contentDescription`), fontes escaláveis do sistema operacional (`sp`) e Modo Escuro nativo.
* **Internacionalização (i18n):** Suporte nativo a dois idiomas (Português e Inglês).

---

##  Estrutura do Repositório (Monorepo)

O projeto está dividido nas seguintes frentes de trabalho:

```text
minha-estante/
├── android-app/        # App do usuário (Kotlin + Jetpack Compose)
├── admin-dashboard/    # Painel Administrativo (React Native)
├── firebase/           # Regras de segurança e infraestrutura
└── README.md           # Este guia
```

---

##  Como Clonar e Executar o Projeto

**Atenção Equipe e Avaliadores:** Como este é um monorepo com múltiplas tecnologias, siga rigorosamente as instruções abaixo para a plataforma que deseja rodar, para evitar erros de módulos não encontrados.

### Passo 1: Clonar o Repositório
Abra o seu terminal e execute:

```bash
git clone https://github.com/SEU_USUARIO/minha-estante.git
```

###  Passo 2: Rodar o Aplicativo Android (Kotlin)
O aplicativo principal deve ser aberto **exclusivamente** no Android Studio.

1. Abra o Android Studio e clique em **Open**.
2. Navegue até a pasta clonada (`minha-estante`).
3. **MUITO IMPORTANTE:** Não abra a pasta raiz. Clique **APENAS na pasta `android-app`** e clique em OK.
4. Aguarde o *Gradle Sync* terminar de baixar as dependências (uma barra de carregamento no canto inferior direito).
5. Quando o carregamento concluir, aperte o botão de **Play (Run)** para iniciar o emulador.

###  Passo 3: Rodar o Painel Administrativo (React Native)
O painel administrativo deve ser aberto preferencialmente no **VS Code**.

1. Abra a pasta raiz `minha-estante` no VS Code.
2. Abra o terminal integrado do VS Code e navegue para a pasta do painel:

```bash
cd admin-dashboard
```

3. Instale as dependências (Node Modules):

```bash
npm install
```

4. Inicie o servidor do Expo:

```bash
npx expo start
```

refaça esse readme 
