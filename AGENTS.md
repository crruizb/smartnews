# AGENTS.md

Repo with two independent apps: `backend/` (Spring Boot, Java 21, Maven) and `frontend/` (React 19, Vite, TypeScript, pnpm). There is no root build — operate inside `backend/` or `frontend/` separately.

## Backend (`backend/`)

### Run
```bash
docker compose -f backend/ops/docker-compose.yml up -d   # Postgres on :7000 (user/user, db smart_news)
mvn -f backend spring-boot:run                              # app on :8080
```
Default JDBC URL points at `localhost:7000/smart_news`; CI validates with `mvn -f backend clean verify`.

### Required env vars at runtime
`OPENAI_API_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`. Defaults exist for `JWT_SECRET`, `JDBC_DATABASE_*`, `FRONTEND_URL`, `BACKEND_DOMAIN`, `GOOGLE_OAUTH2_REDIRECT` (see `src/main/resources/application.yml`). Missing required vars will crash startup of the AI / OAuth2 features.

### Layout
- Package root: `com.github.cristianrb.smartnews`
- `rest/` — controllers (`AuthController`, `ContributionController`)
- `service/contributions/` — domain services; `service/ai/` — Spring AI (`gpt-4o-mini`) + MCP recommendation/tool-calling
- `handler/` — one `*Handler` per newspaper source (ABC, El País, El Mundo, Marca, La Vanguardia, NYTimes, …); `GenericHandler` is the base
- `rss/RSSDownloader` — scheduled RSS fetch
- `auth/` — OAuth2 (Google) + JWT (`JwtTokenProvider`, `JwtTokenFilter`, `CORSFilter`)
- `entity/` — JPA entities; `repository/` — Spring Data repos
- `config/` — `WebSecurityConfig`, `CorsConfig`, `SpringContextConfig`

### Migrations (Flyway)
`src/main/resources/db/migration/V00x__*.sql`. Flyway is enabled with `validate-on-migrate`. Never edit an existing migration — add a new `V006__…` file. Schema changes must be reflected on `entity/` classes too.

### Tests
Only `ContributionControllerTests`, `ContributionsServiceTests` (JUnit + Mockito). Run one: `mvn -f backend test -Dtest=ContributionsServiceTests`. Backend tests run against the real Postgres in CI only when the context loads; most tests are unit-level with Mockito — do not assume a live DB is available locally without `docker compose up`.

### Deploy
CI (`.github/workflows/ci.yml`) builds on push to `master` and PRs; on `master` it pushes a Docker image to DockerHub using the pre-built `target/*.jar`. Local `Dockerfile` just wraps an already-built jar (`mvn package` first) — it does not build inside the image.

## Frontend (`frontend/`)

- Node 20 (`.nvmrc`), pnpm, Vite with `@vitejs/plugin-react-swc` and Tailwind v4 via `@tailwindcss/vite`.
- `pnpm install && pnpm run dev` (Vite dev server, port 5173).
- Build: `pnpm run build` runs `tsc -b && vite build` — typecheck failures fail the build.
- Lint: `pnpm lint` (eslint flat config in `eslint.config.js`). No formatter is configured; no test runner is configured.
- Stacks in use: `@tanstack/react-query`, `react-router 7`, `i18next` (+ `i18next-http-backend` for locale files), `react-hot-toast`, `js-cookie`. State/data fetching goes through react-query hooks in `features/*/use*.ts`; do not add axios/fetch-duplication — extend `services/apiContributions.ts`.

### Layout
- `features/contributions/` — feature components + `useContributions` query hook
- `pages/` — route-level views; `ui/` — shared layout (`AppLayout`, `Header`, `Sidebar`, `DarkMode`, `LanguageSelector`)
- `services/apiContributions.ts` — single API client; backend base URL comes from Vite env
- Deployment is Cloudflare Pages (`smartnews.pages.dev`), not in CI here — verify build works with `pnpm run build` before considering the FE done.

## Conventions

- Backend is Java (`pom.xml` parent Spring Boot 3.5.6, Java 21); prefer existing patterns in `service/contributions` for new services (interface + `*Impl`).
- Adding a new RSS source = new `*Handler extends GenericHandler` under `handler/`. Don't dump parsing logic elsewhere.
- Frontend uses Tailwind utility classes; do not introduce CSS frameworks. Tailwind config is implicit (v4, via the Vite plugin) — there is no `tailwind.config.js`.
- `application.yml` uses env placeholders with sensible local defaults — preserve the `${VAR:default}` pattern when adding config.
- Do not commit secrets; `.gitignore` is minimal (`.idea`, `.DS_STORE`).

## Agent skills

### Issue tracker

Local markdown — issues live as files under `.scratch/<feature-slug>/` in this repo. See `docs/agents/issue-tracker.md`.

### Triage labels

Default vocabulary: `needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context. See `docs/agents/domain.md`.