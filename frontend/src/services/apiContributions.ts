let API_URL = "https://smartnews-be.cristianruiz.dev/sn/api";
const isLocalhost =
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1";
if (isLocalhost) {
  API_URL = "http://localhost:8080/api";
}

export async function getLatestContributions(
  pageParam: number,
  sourceFilter: string,
  dateFilter: string
) {
  const queryParams = new URLSearchParams();
  queryParams.append("page", pageParam.toString());
  if (sourceFilter) {
    queryParams.append("source", sourceFilter);
  }
  if (dateFilter) {
    queryParams.append("date", dateFilter);
  }

  const res = await fetch(`${API_URL}/latest?${queryParams.toString()}`);

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
