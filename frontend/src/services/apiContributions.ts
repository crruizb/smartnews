import { ApiContribution } from "../types";

let API_URL = "https://api.cristianruiz.dev/sn/api";
const isLocalhost =
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1";
if (isLocalhost) {
  API_URL = "http://localhost:8080/api";
}

const AUTH_URL = API_URL.replace(/\/api$/, "/auth");

/**
 * Logout is a top-level navigation (not a fetch): the backend clears the
 * httpOnly auth cookies in the response and redirects back to the app. Cookie
 * clearing on a navigation is as reliable as it is on login.
 */
export function getLogoutUrl() {
  return `${AUTH_URL}/logout`;
}

export async function getLatestContributions(
  pageParam: number,
  sourceFilter: string
) {
  const queryParams = new URLSearchParams();
  queryParams.append("page", pageParam.toString());
  if (sourceFilter) {
    queryParams.append("source", sourceFilter);
  }

  const res = await fetch(`${API_URL}/latest?${queryParams.toString()}`, {
    credentials: "include",
  });

  if (!res.ok) throw Error("Could fetch latest contributions");

  const { data } = await res.json();
  return data;
}

export async function getContribution(id: number): Promise<ApiContribution> {
  const res = await fetch(`${API_URL}/contributions?id=${id}`, {
    credentials: "include",
  });
  if (!res.ok) throw Error(`Could fetch contribution id ${id}`);

  return res.json();
}

export async function getRatedContributions(pageParam: number) {
  const queryParams = new URLSearchParams();
  queryParams.append("page", pageParam.toString());

  const res = await fetch(`${API_URL}/rated?${queryParams.toString()}`, {
    credentials: "include",
  });

  if (!res.ok) throw Error("Could fetch rated contributions");

  const data = await res.json();
  return data;
}

export async function getRecommendations(pageParam: number) {
  const queryParams = new URLSearchParams();
  queryParams.append("page", pageParam.toString());

  const res = await fetch(`${API_URL}/recommendations?${queryParams.toString()}`, {
    credentials: "include",
  });

  if (!res.ok) throw Error("Could fetch recommendations");

  const data = await res.json();
  return data;
}

export async function searchContributions(query: string, pageParam: number) {
  const queryParams = new URLSearchParams();
  queryParams.append("q", query);
  queryParams.append("page", pageParam.toString());

  const res = await fetch(`${API_URL}/search?${queryParams.toString()}`, {
    credentials: "include",
  });

  if (!res.ok) throw Error("Could not search contributions");

  const data = await res.json();
  return data;
}

export async function voteContribution(id: number, rating: number) {
  const res = await fetch(`${API_URL}/contributions/${id}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(rating),
    credentials: "include",
  });

  if (!res.ok) throw Error(`Could not vote contribution id ${id}`);
}
