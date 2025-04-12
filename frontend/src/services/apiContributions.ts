let API_URL = "https://api.cristianruiz.dev/sn/api";
const isLocalhost =
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1";
if (isLocalhost) {
  API_URL = "http://localhost:8080/api";
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

export async function getContribution(id: number) {
  const res = await fetch(`${API_URL}/contribution/${id}`);
  if (!res.ok) throw Error(`Could fetch contribution id ${id}`);

  const { data } = await res.json();
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
