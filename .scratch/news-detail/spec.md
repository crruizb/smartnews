# Contribution detail page

Status: `ready-for-agent`

## Problem Statement

When a reader clicks a Contribution in SmartNews, they are sent straight to the newspaper's external site in a new tab. The reader leaves SmartNews, cannot see the stored teaser or metadata in a focused view, and loses their place in the list. There is no in-app page for a single Contribution to link to, share, or refresh.

## Solution

Clicking a Contribution opens an in-app detail page at a deep-linkable route. The page shows the Contribution's stored content (title, teaser, image, metadata, categories) and lets the reader rate it, with a single clear, explicit action to open the full article on the original Source. The route is public, so it works for anonymous readers and signed-in readers alike.

Because the backend stores only the RSS teaser — it has never stored an article body — the detail page presents what SmartNews already owns and does not attempt to reproduce the full article.

## User Stories

1. As a reader, I want clicking a Contribution card to open an in-app detail page, so that I stay within SmartNews instead of being pulled to an external site.
2. As a reader, I want the detail page to show the Contribution's title, so that I know which article I opened.
3. As a reader, I want the detail page to show the teaser, so that I can decide whether the full article is worth opening.
4. As a reader, I want the detail page to show the hero image when one exists, so that the page feels like a real article view.
5. As a reader, I want the detail page to show the Source, creator, and publication date, so that I can judge the article's provenance and recency.
6. As a reader, I want the detail page to show the Contribution's categories, so that I understand its topic.
7. As a reader, I want to rate the Contribution from the detail page, so that I can give feedback without going back to the list.
8. As a signed-in reader, I want my existing vote for the Contribution to be pre-selected on the detail page, so that I see my own rating.
9. As a reader, I want a prominent "Read full article on <source>" button, so that I can deliberately continue to the newspaper when I choose to.
10. As a reader, I do not want the teaser rendered as raw HTML, so that a malformed or malicious feed cannot inject markup into the page.
11. As a reader, I want a back control on the detail page, so that I can return to the list.
12. As a reader, I want to deep-link to a Contribution's detail page, so that the URL survives a refresh and can be shared.
13. As a reader, I want a clear not-found state when a Contribution does not exist or the id is invalid, so that I am not shown an endless loading indicator or a broken page.
14. As a reader, I want the not-found state to link back to the list, so that I can recover.
15. As an anonymous reader, I want to view a detail page without logging in, so that I can read before signing up.
16. As a signed-in reader, I want the detail page to still work for me, so that I get my vote state while anonymous readers get the same content without it.
17. As a reader, I want the detail page available in both supported languages, so that its labels match my language setting.
18. As a reader, I want the card's rating control and category tags to remain independently usable, so that clicking a star or a tag does not navigate away.

## Implementation Decisions

- **Domain vocabulary**: the canonical term is **Contribution**. "News"/"article"/"item"/"post" are avoided; the term is recorded in the root `CONTEXT.md` glossary.
- **Route**: a real, deep-linkable frontend route for a single Contribution, parameterized by id, rendered inside the existing app layout. The default section state of the list page is not preserved on back-navigation (accepted for this slice).
- **Data access**: a new single-Contribution query hook in the contributions feature, backed by the existing API client module. The client function for a single Contribution is corrected to the backend's actual contract and sends credentials so signed-in readers keep their vote.
- **API contract**: the existing `GET /api/contributions?id=<id>` endpoint is reused. It returns a single Contribution object (not a `{ data }` envelope). No new endpoint.
- **Public access**: the endpoint is made null-safe for an anonymous `Principal`, returning the Contribution with a null vote. Signed-in callers continue to receive their vote. This mirrors the null-safe principal handling already used by the list and search endpoints.
- **No schema change**: no new column, no migration. The detail page shows the stored teaser only; the article body is never fetched or persisted.
- **Card interaction**: the card's external anchor is replaced with an internal link to the detail route. The rating control and category tags remain outside the link so they stay independently clickable.
- **Detail page content**: back control; title; meta line of source, creator, and publication date; hero image when present; teaser rendered as plain text; category tags; the rating control; and a primary external button to the original article that opens in a new tab with `rel="noopener noreferrer"`.
- **Not-found handling**: a non-numeric id and a failing lookup both render the same in-page not-found state with a link back to the list. Query retry is disabled so a missing Contribution fails fast rather than hanging in a loading state.
- **i18n**: a new `detail` namespace with loading, not-found, back, and read-more labels, kept in sync across the English and Spanish locale files.

## Testing Decisions

A good test here observes external behaviour through a public seam: given an anonymous caller, the endpoint returns the Contribution with no vote; it does not assert on internals, mocks of private collaborators, or database state.

- **Backend seam** (one, existing): the `ContributionController` endpoint for a single Contribution. Prior art is the existing `ContributionControllerTests`, which uses JUnit + Mockito and stubs the contributions service. Add a case asserting that an anonymous caller (null principal) receives the Contribution with a null vote, alongside the existing tests.
- **Frontend**: no test runner is configured and none is introduced. Verification is `tsc`/`vite build`, `eslint`, and a manual pass over the list → detail → back → external-link flow.
- The previous subtle bug (endless loading for an invalid id) is covered by the not-found behaviour decision above; it is verified manually on the frontend because there is no frontend test seam.

## Out of Scope

- Fetching, scraping, or persisting the full article body (a `content` column and ingest-time extraction are explicitly rejected here).
- Any reading-time, comments, or social-sharing feature on the detail page.
- Preserving the list's active section or search in the URL across back-navigation.
- Extracting shared card/detail UI components (meta block, category chips, rating fallback); the duplication is accepted for this slice.
- Any change to the vote/rating backend beyond reading the existing per-user vote on the detail endpoint.

## Further Notes

- The backend stores only the RSS teaser (`description`); this is the central constraint that defines the feature. If full-article display is ever wanted, it is a separate effort (scrape at request time or persist at ingest time), with its own ToS and reliability concerns.
- The existing single-Contribution API client function previously pointed at a non-existent path and unwrapped a `{ data }` envelope the endpoint never returned; both are corrected as part of this work.
- The agreed seams were confirmed before implementation: one backend controller seam with a unit test; frontend verified by build, lint, and manual check.
