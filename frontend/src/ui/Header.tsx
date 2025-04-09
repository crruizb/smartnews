import DarkMode from "./DarkMode";
import Cookies from "js-cookie";

export default function Header() {
  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  const username = Cookies.get("username");
  console.log(username);

  return (
    <header className="flex justify-between items-center py-4 px-8 bg-white dark:bg-[#2c2c2c] sticky top-0 z-50 border-b border-pink shadow-md dark:shadow-palid-purple dark:shadow-xs">
      <h1 className="text-xl md:text-2xl font-semibold">📰 Smart News</h1>
      <DarkMode />
      {!username && <button onClick={handleLogin}>Login with Google</button>}
    </header>
  );
}
