import { ChangeEventHandler } from "react";

function DarkMode() {
  const setDark = () => {
    localStorage.setItem("theme", "dark");
    document.documentElement.setAttribute("data-theme", "dark");
    document.documentElement.classList.add("dark");
  };

  const setLight = () => {
    localStorage.setItem("theme", "light");
    document.documentElement.setAttribute("data-theme", "light");
    document.documentElement.classList.remove("dark");
  };

  const storedTheme = localStorage.getItem("theme");

  const prefersDark =
    window.matchMedia &&
    window.matchMedia("(prefers-color-scheme: dark)").matches;

  const defaultDark =
    storedTheme === "dark" || (storedTheme === null && prefersDark);

  if (defaultDark) {
    setDark();
  }

  const toggleTheme: ChangeEventHandler<HTMLInputElement> = (e) => {
    if (e.target.checked) {
      setDark();
    } else {
      setLight();
    }
  };

  return (
    <div className="flex gap-1 items-center">
      <span className="text-xl">☀️</span>
      <label htmlFor="checkbox">
        <div className="relative cursor-pointer">
          <input
            type="checkbox"
            id="checkbox"
            className="sr-only"
            onChange={toggleTheme}
          />

          <div className="block bg-gray-600 w-14 h-6 rounded-full"></div>

          <div className="dot absolute left-1 top-1 bg-white w-6 h-4 rounded-full transition"></div>
        </div>
        {/* <input type="checkbox" id="checkbox" onChange={toggleTheme} /> */}
      </label>
      <span className="text-xl">🌒</span>
    </div>
  );
}

export default DarkMode;
