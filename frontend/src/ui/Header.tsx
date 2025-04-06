import DarkMode from "./DarkMode";

export default function Header() {
  return (
    <header className="flex justify-between items-center py-4 px-8 bg-white dark:bg-[#2c2c2c] sticky top-0 z-50 border-b border-pink shadow-md dark:shadow-palid-purple dark:shadow-xs">
      <h1 className="text-xl md:text-2xl font-semibold">📰 Smart News</h1>
      <DarkMode />
    </header>
  );
}
