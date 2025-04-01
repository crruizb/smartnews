const API_URL = "http://localhost:8080/api";

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

  console.log(`${API_URL}/latest?${queryParams.toString()}`);
  const res = await fetch(`${API_URL}/latest?${queryParams.toString()}`);

  if (!res.ok) throw Error("Could fetch latest contributions");

  const { data } = await res.json();
  console.log(data);
  return data;
}

export async function getContribution(id: number) {
  const res = await fetch(`${API_URL}/contribution/${id}`);
  if (!res.ok) throw Error(`Could fetch contribution id ${id}`);

  const { data } = await res.json();
  return data;
}
